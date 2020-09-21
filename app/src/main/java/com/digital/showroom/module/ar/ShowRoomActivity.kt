package com.digital.showroom.module.ar

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.digital.showroom.R
import com.digital.showroom.module.ar.ui.main.ShowRoomViewModel
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.AppConstants
import com.digital.showroom.utils.AppConstants.Companion.KEY_INTENT_POSITION
import com.digital.showroom.utils.BaseViewModelFactory
import com.digital.showroom.utils.Logger
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.show_room_activity.*


class ShowRoomActivity : AppCompatActivity() {

    private lateinit var viewModel: ShowRoomViewModel
    private var anchor: Anchor? = null
    private lateinit var arFragment: ArFragment
    private lateinit var renderableAsset: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room_activity)
        initView()
        initData()
    }

    private fun initData() {
        val position: Int = intent.getIntExtra(KEY_INTENT_POSITION, 0)
        viewModel =
            ViewModelProvider(this, BaseViewModelFactory { ShowRoomViewModel(position) }).get(
                ShowRoomViewModel::class.java
            )
//        viewModel = ViewModelProvider(this).get(ShowRoomViewModel::class.java)

        //Model is hardcoded for demo purposes
        if (position == 6) {
            viewModel.getModelName(position)
        } else {
            DataRepository.renderableAsset?.let { onRenderSuccess(it) };
        }
        viewModel.model.observe(this, Observer {
            if (it.isNotEmpty()) renderGlbModel(it) else onDownloadFailed()
        })

        if (viewModel.isColorPaletAvailable(position)) {
            color_layout.visibility = View.VISIBLE
        }

//        viewModel.startDownloading(position);
//        viewModel.modelFileName.observe(this, Observer {
//            if (it == null) onDownloadFailed() else renderGlbModel(it)
//        })
    }

    private fun initView() {
        arFragment = sceneform_fragment as ArFragment
        arFragment.planeDiscoveryController.hide()
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor();
            addNodeToScene(arFragment, anchor, renderableAsset)
        }

        color_red_iv.setOnClickListener {
            renderGlbModel(AppConstants.INTREGA_RED_MODEL_GLB_NAME)
            updateSelectedColor(AppConstants.INTREGA_RED_MODEL_GLB_NAME);
        }
        color_blue_iv.setOnClickListener {
            renderGlbModel(AppConstants.INTREGA_BLUE_MODEL_GLB_NAME)
            updateSelectedColor(AppConstants.INTREGA_BLUE_MODEL_GLB_NAME);
        }
        color_white_iv.setOnClickListener {
            renderGlbModel(AppConstants.INTREGA_MODEL_GLB_NAME)
            updateSelectedColor(AppConstants.INTREGA_MODEL_GLB_NAME);
        }
    }

    private fun updateSelectedColor(modelName: String) {
        when (modelName) {
            AppConstants.INTREGA_RED_MODEL_GLB_NAME -> {
                selected_color.text = getString(R.string.red)
                selected_color.setTextColor(getColor(R.color.red_dark))
            }
            AppConstants.INTREGA_BLUE_MODEL_GLB_NAME -> {
                selected_color.text = getString(R.string.blue)
                selected_color.setTextColor(getColor(R.color.blue))
            }
            else -> {
                selected_color.text = getString(R.string.white)
                selected_color.setTextColor(getColor(R.color.white))
            }
        }
    }

    private fun renderGlbModel(modelName: String?) {
        showProgressbar(getString(R.string.rendering))
        Logger.log("renderGlbModel $modelName")
        val renderableSource = RenderableSource
            .builder()
            .setSource(this, Uri.parse(modelName), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()

        ModelRenderable
            .builder()
            .setSource(this@ShowRoomActivity, renderableSource)
            .setRegistryId(Uri.parse(modelName))
            .build()
            .thenAccept { modelRenderable ->
                Logger.log("$modelName rendered")
                onRenderSuccess(modelRenderable);
            }.exceptionally {
                Logger.log("$modelName render failed" + it.localizedMessage)
                onDownloadFailed()
                null
            }

    }

    private fun onRenderSuccess(modelRenderable: ModelRenderable) {
        renderableAsset = modelRenderable
        hideProgressbar()
    }

    private fun onDownloadFailed() {
        hideProgressbar()
    }

    private fun hideProgressbar() {
        Logger.log("gone")
        progress_status.visibility = View.GONE
    }

    private fun showProgressbar(status: String = getString(R.string.downloading)) {
        Logger.log("visible")
        progress_status.visibility = View.VISIBLE
        progress_status.text = status
    }


    /**
     * @param fragment our fragment
     * @param anchor ARCore anchor
     * @param renderable our model created as a Sceneform Renderable
     *
     * This method builds two nodes and attaches them to our scene
     * The Anchor nodes is positioned based on the pose of an ARCore Anchor. They stay positioned in the sample place relative to the real world.
     * The Transformable node is our Model
     * Once the nodes are connected we select the TransformableNode so it is available for interactions
     */
    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor?, renderable: ModelRenderable) {
        anchor?.let {
            val anchorNode = AnchorNode(anchor)
            // TransformableNode means the user to move, scale and rotate the model
            val transformableNode = TransformableNode(fragment.transformationSystem)
            transformableNode.renderable = renderable
            transformableNode.setParent(anchorNode)
            fragment.arSceneView.scene.addChild(anchorNode)
            transformableNode.select()
        }
    }
}