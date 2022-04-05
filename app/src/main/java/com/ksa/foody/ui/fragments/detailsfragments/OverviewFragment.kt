package com.ksa.foody.ui.fragments.detailsfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.ksa.foody.R
import com.ksa.foody.databinding.FragmentOverviewBinding
import com.ksa.foody.models.Result
import com.ksa.foody.util.Constants.Companion.RECIPE_BUNDLE
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    private var _binding : FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater,container, false)

        val args = arguments
        val myBundle : Result? = args?.getParcelable(RECIPE_BUNDLE)
        binding.mainImageview.load(myBundle?.image)
        binding.titleTextView.text = myBundle?.title
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()
        binding.timeTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding.summaryTextView.text = summary
        }


        if(myBundle?.vegan == true){
            binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.vegetarian == true){
            binding.vegitarianImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.vegitarianTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.glutenFree == true){
            binding.glutenfreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.glutenfreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.dairyFree == true){
            binding.dairyfreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.dairyfreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.veryHealthy == true){
            binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.cheap == true){
            binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}