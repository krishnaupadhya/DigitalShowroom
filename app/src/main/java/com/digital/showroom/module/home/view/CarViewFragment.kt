

package com.digital.showroom.module.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.digital.showroom.R
import com.digital.showroom.databinding.FragmentCarViewBinding
import com.digital.showroom.module.ar.ShowRoomActivity
import com.digital.showroom.module.home.viewmodel.CarViewModel
import com.digital.showroom.module.home.viewmodel.HomeViewModel
import com.digital.showroom.utils.AppConstants
import com.digital.showroom.utils.AppConstants.Companion.KEY_INTENT_POSITION
import kotlinx.android.synthetic.main.fragment_car_view.*
import kotlinx.android.synthetic.main.home_fragment.*

class CarViewFragment : Fragment() {

    private lateinit var carViewBinding: FragmentCarViewBinding
    private lateinit var viewModel: CarViewModel

    companion object {


        fun getInstance(position: Int): Fragment {
            val carViewFragment = CarViewFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_INTENT_POSITION, position)
            carViewFragment.arguments = bundle
            return carViewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         carViewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_car_view, container, false)
       return carViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(KEY_INTENT_POSITION)
        viewModel =ViewModelProvider(this).get(CarViewModel::class.java)
        carViewBinding.carviewmodel = viewModel
        carViewBinding.lifecycleOwner = this
        viewModel.getItemAtPosition(position)
        view_in_3d.setOnClickListener {
            val intent = Intent(activity, ShowRoomActivity::class.java)
            intent.putExtra(AppConstants.KEY_INTENT_POSITION, position)
            startActivity(intent)
        }
    }

}
