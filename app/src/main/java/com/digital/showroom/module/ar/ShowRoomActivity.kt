package com.digital.showroom.module.ar

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.digital.showroom.R
import com.digital.showroom.module.ar.ui.main.ShowRoomViewModel
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.AppConstants
import com.digital.showroom.utils.AppConstants.Companion.KEY_INTENT_POSITION
import com.digital.showroom.utils.Logger
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.show_room_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class ShowRoomActivity : AppCompatActivity() {

    private var anchor: Anchor? = null
    private lateinit var arFragment: ArFragment
    private lateinit var renderableAsset: ModelRenderable
    private lateinit var viewModel: ShowRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room_activity)
        viewModel = ViewModelProvider(this).get(ShowRoomViewModel::class.java)

//        val position: Int = intent.getIntExtra(KEY_INTENT_POSITION, 0)
//        viewModel.startDownloading(position);
//        viewModel.modelFileName.observe(this, Observer {
//            if (it == null) onDownloadFailed() else renderGlbModel(it)
//        })

        //Model is hardoded for demo purposes
        if (DataRepository.renderableAsset != null) {
            renderableAsset = DataRepository.renderableAsset!!
            initArPlane()
        }
    }

    private fun initArPlane() {
        arFragment = sceneform_fragment as ArFragment
        arFragment.planeDiscoveryController.hide()
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor();
            addNodeToScene(arFragment, anchor, renderableAsset)
        }
    }

    private fun renderGlbModel() {
        showProgressbar(getString(R.string.rendering))
        Logger.log("renderGlbModel")
        val renderableSource = RenderableSource
            .builder()
            .setSource(this, Uri.parse(AppConstants.CIVIC_MODEL_GLB_NAME), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()

        ModelRenderable
            .builder()
            .setSource(this@ShowRoomActivity, renderableSource)
            .setRegistryId(Uri.parse(AppConstants.CIVIC_MODEL_GLB_NAME))
            .build()
            .thenAccept { modelRenderable ->
                Logger.log("rendered")
                onRenderSuccess(modelRenderable);
            }.exceptionally {
                Logger.log("render failed" + it.localizedMessage)
                onDownloadFailed()
                null
            }

    }

    private fun onRenderSuccess(modelRenderable: ModelRenderable) {
        runOnUiThread {
            renderableAsset = modelRenderable
            initArPlane()
            onDownloadFailed()
        }
    }

    private fun onDownloadFailed() {
        hideProgressbar()
    }

    private fun hideProgressbar() {
        Logger.log("gone")
        progress_bar.visibility = View.GONE
        progress_status.visibility = View.GONE
    }

    private fun showProgressbar(status: String = getString(R.string.downloading)) {
        Logger.log("visible")
        progress_bar.visibility = View.VISIBLE
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