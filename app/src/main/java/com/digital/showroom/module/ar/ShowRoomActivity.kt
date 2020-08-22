package com.digital.showroom.module.ar

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.digital.showroom.R
import com.digital.showroom.module.ar.ui.main.ShowRoomViewModel
import com.digital.showroom.utils.AppConstants.Companion.KEY_INTENT_POSITION
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.show_room_fragment.*
import java.io.File


class ShowRoomActivity : AppCompatActivity() {

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
        initArView()
        viewModel = ViewModelProvider(this).get(ShowRoomViewModel::class.java)
        val position: Int = intent.getIntExtra(KEY_INTENT_POSITION, 0)
        viewModel.downLoadModel(position);
        viewModel.modelFileName.observe(this, Observer {
            renderGlbModel(it)
        })
    }

    private fun initArView() {
        arFragment = sceneform_fragment as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.renderable = renderableAsset
            arFragment.arSceneView.scene.addChild(anchorNode)
        }


    }


    private fun renderGltfModel() {
        val renderableSource = RenderableSource
            .builder()
            .setSource(this, Uri.parse(GLTF_ASSET_2), RenderableSource.SourceType.GLTF2)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()

        ModelRenderable
            .builder()
            .setSource(this, renderableSource)
            .setRegistryId(GLTF_ASSET_2)
            .build()
            .thenAccept { modelRenderable ->
                renderableAsset = modelRenderable
            }.exceptionally {
                val toast = Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
                toast.show()
                null
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
            .setRegistryId(GLTF_ASSET_1)
            .build()
            .thenAccept { modelRenderable ->
                renderableAsset = modelRenderable
                Log.d("model", "rendered")
            }.exceptionally {
                Log.d("model", "render failed" + it.localizedMessage)
                null
            }
    }
}