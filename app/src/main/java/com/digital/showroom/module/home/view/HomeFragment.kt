package com.digital.showroom.module.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.digital.showroom.R
import com.digital.showroom.model.CarData
import com.digital.showroom.module.ar.ShowRoomActivity
import com.digital.showroom.module.home.viewmodel.HomeViewModel
import com.digital.showroom.utils.AppConstants
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var dots: Array<ImageView?>
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_in_3d.setOnClickListener {
            val intent = Intent(activity, ShowRoomActivity::class.java)
            intent.putExtra(AppConstants.KEY_INTENT_POSITION, view_pager_car.currentItem)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(HomeViewModel::class.java) }!!
        viewModel.carsList.observe(viewLifecycleOwner, Observer {
            setPagerAdapter(it)
        })
    }

    fun setPagerAdapter(carList: List<CarData>) {
        val carViewAdapter = CarViewAdapter(activity as AppCompatActivity, carList.size)
        view_pager_car.adapter = carViewAdapter
        view_pager_car.registerOnPageChangeCallback(pageChangeCallback)
        dots = arrayOfNulls<ImageView>(carList.size)

        for (i in carList.indices) {
            dots[i] = ImageView(activity)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    activity as AppCompatActivity,
                    R.drawable.non_active_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            slider_dots.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                activity as AppCompatActivity,
                R.drawable.active_dot
            )
        )
    }

    private var pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.getItemAtPosition(position)
            for (element in dots) {
                element?.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity as AppCompatActivity,
                        R.drawable.non_active_dot
                    )
                )
            }
            dots[position]?.setImageDrawable(
                ContextCompat.getDrawable(
                    activity as AppCompatActivity,
                    R.drawable.active_dot
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view_pager_car.unregisterOnPageChangeCallback(pageChangeCallback)
    }


}