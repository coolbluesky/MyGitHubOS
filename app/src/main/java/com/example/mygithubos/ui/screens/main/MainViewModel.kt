package com.example.mygithubos.ui.screens.main

import androidx.lifecycle.ViewModel
import com.example.mygithubos.domain.usecase.IsAuthenticatedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : ViewModel() {
    val isAuthenticated: Flow<Boolean> = isAuthenticatedUseCase()
} 