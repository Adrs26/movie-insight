package com.example.movieinsight.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.movieinsight.R
import com.example.movieinsight.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGlide()
        setupButton()
    }

    private fun setupButton() {
        binding.ivGithub.setOnClickListener {
            openApp(
                "com.github.android",
                "https://github.com/Adrs26"
            )
        }
        binding.ivLinkedin.setOnClickListener {
            openApp(
                "com.linkedin.android",
                "https://www.linkedin.com/in/adrian-septiyadi-9ba26b25a"
            )
        }
        binding.ivInstagram.setOnClickListener {
            openApp(
                "com.instagram.android",
                "https://instagram.com/adriansept26"
            )
        }
    }

    private fun setupGlide() {
        Glide.with(this)
            .load(R.drawable.profile)
            .centerCrop()
            .transform(CircleCrop())
            .into(binding.ivUserPhoto)
    }

    private fun openApp(packageName: String, uri: String) {
        val intent = requireActivity().packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.data = Uri.parse(uri)
            startActivity(intent)
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }
}