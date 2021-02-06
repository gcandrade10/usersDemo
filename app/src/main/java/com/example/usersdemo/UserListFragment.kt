package com.example.usersdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.usersdemo.databinding.FragmentUserListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersRenderer: UsersRenderer
    private val userListViewModel: UserListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersRenderer = UsersRenderer(binding.usersList, object : UsersRenderer.RendererListener {
            override fun onUserItem(user: User) {
                findNavController().navigate(UserListFragmentDirections.actionUserListFragmentToDetailFragment(user))
            }

            override fun onLoadMore() {
                binding.loadingIndicator.visibility = View.VISIBLE
                userListViewModel.loadMore()
            }
        })
        binding.loadingIndicator.visibility = View.VISIBLE
        userListViewModel.users().observe(viewLifecycleOwner, {
            usersRenderer.render(it)
            binding.loadingIndicator.visibility = View.GONE
        })

        userListViewModel.moreLiveData.observe(viewLifecycleOwner, {
            usersRenderer.addMore(it)
            binding.loadingIndicator.visibility = View.GONE
        })

        userListViewModel.errorLiveData.observe(viewLifecycleOwner, {
            binding.loadingIndicator.visibility = View.GONE
            Toast.makeText(requireActivity(), R.string.error, Toast.LENGTH_SHORT).show()
        })

    }


}