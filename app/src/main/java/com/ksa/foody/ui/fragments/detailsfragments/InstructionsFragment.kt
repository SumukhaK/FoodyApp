package com.ksa.foody.ui.fragments.detailsfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.ksa.foody.R
import com.ksa.foody.databinding.FragmentInstructionsBinding
import com.ksa.foody.models.Result
import com.ksa.foody.util.Constants



class InstructionsFragment : Fragment() {

    private var _binding : FragmentInstructionsBinding? = null
    private val binding get()  = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentInstructionsBinding.inflate (inflater, container, false)

        val args = arguments
        val myBundle : Result? = args?.getParcelable(Constants.RECIPE_BUNDLE)
        val instructionSourceURL = myBundle!!.sourceUrl
        binding.instructionsWebview.webViewClient = object : WebViewClient () {}
        binding.instructionsWebview.loadUrl(instructionSourceURL)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}