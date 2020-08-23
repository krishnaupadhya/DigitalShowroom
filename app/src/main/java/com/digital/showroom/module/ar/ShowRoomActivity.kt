package com.digital.showroom.module.ar

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.digital.showroom.R
import com.digital.showroom.module.ar.ui.main.ShowRoomViewModel
import com.digital.showroom.utils.AppConstants.Companion.KEY_INTENT_POSITION
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.show_room_fragment.*
import java.io.File


class ShowRoomActivity : AppCompatActivity() {

    private var anchor: Anchor? = null
    private lateinit var arFragment: ArFragment
    private lateinit var renderableAsset: ModelRenderable
    private lateinit var viewModel: ShowRoomViewModel
    private val GLTF_ASSET =
        "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf"
    private val GLTF_ASSET_2 =
        "https://github.com/krishnaupadhya/ArCoreModel/blob/master/model/car3/Convertible.gltf"
    private val GLTF_ASSET_1 =
        "https://github.com/krishnaupadhya/ArCoreModel/blob/master/model/car2/out.glb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_room_activity)
        viewModel = ViewModelProvider(this).get(ShowRoomViewModel::class.java)
        val position: Int = intent.getIntExtra(KEY_INTENT_POSITION, 0)
        viewModel.downLoadModel(position);
        viewModel.modelFileName.observe(this, Observer {
            renderGlbModel(it)
        })
    }

    private fun initArPlane() {
        arFragment = sceneform_fragment as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor();
            addNodeToScene(arFragment, anchor, renderableAsset)
        }
    }

    private fun renderGlbModel(file: File) {
        val renderableSource = RenderableSource
            .builder()
            .setSource(this, Uri.parse(file.path), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()

        ModelRenderable
            .builder()
            .setSource(this, renderableSource)
            .setRegistryId( Uri.parse(file.path))
            .build()
            .thenAccept { modelRenderable ->
                renderableAsset = modelRenderable
                initArPlane()
                Log.d("model", "rendered")
            }.exceptionally {
                Log.d("model", "render failed" + it.localizedMessage)
                null
            }
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