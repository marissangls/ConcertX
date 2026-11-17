package com.example.concertx.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.fragment.app.Fragment
import com.example.concertx.R
import com.example.concertx.data.local.DatabaseHelper
import com.example.concertx.data.local.SessionManager
import com.example.concertx.ui.auth.LoginActivity

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val username = session.getUsername()
        if (username == null) {
            goToLogin()
            return
        }

        val user = DatabaseHelper(requireContext()).getUserByUsername(username)
        if (user == null) {
            session.clear()
            goToLogin()
            return
        }

        view.findViewById<TextView>(R.id.tvInitial).text =
            user.username.firstOrNull()?.uppercase() ?: ""
        view.findViewById<TextView>(R.id.tvName).text = user.username
        view.findViewById<TextView>(R.id.tvUsername).text = user.username
        view.findViewById<TextView>(R.id.tvEmail).text = user.email
        view.findViewById<TextView>(R.id.tvPhone).text = user.phone

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log out") { _, _ ->
                    session.clear()
                    goToLogin()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}