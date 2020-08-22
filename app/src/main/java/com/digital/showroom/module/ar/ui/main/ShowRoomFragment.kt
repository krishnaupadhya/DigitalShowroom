package com.digital.showroom.module.ar.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.digital.showroom.R
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.show_room_fragment.*


class ShowRoomFragment : Fragment() {


    companion object {
        fun newInstance() = ShowRoomFragment()
    }

    private lateinit var arFragment: ArFragment
    private lateinit var renderableAsset: ModelRenderable
    private lateinit var viewModel: ShowRoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view= inflater.inflate(R.layout.show_room_fragment, container, false)

        initView();
        return view;
    }

    private fun initView() {
        arFragment = sceneform_fragment as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setRenderable(renderableAsset)
            arFragment.arSceneView.scene.addChild(anchorNode)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(ShowRoomViewModel::class.java) }!!

    }



}