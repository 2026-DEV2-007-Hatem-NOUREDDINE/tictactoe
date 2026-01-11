package com.hatem.noureddine.tictactoe.app.di

import com.hatem.noureddine.tictactoe.domain.repository.GameRepository
import com.hatem.noureddine.tictactoe.domain.usecase.GetGameUseCase
import com.hatem.noureddine.tictactoe.domain.usecase.PlayTurnUseCase
import com.hatem.noureddine.tictactoe.domain.usecase.ResetGameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for providing Domain layer dependencies.
 *
 * Although Use Cases are often provided via @Inject on their constructors,
 * this module handles any explicit bindings or third-party domain dependencies if needed.
 * Currently serves as a placeholder for future domain-level bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun providePlayTurnUseCase(repository: GameRepository): PlayTurnUseCase = PlayTurnUseCase(repository)

    @Provides
    fun provideResetGameUseCase(repository: GameRepository): ResetGameUseCase = ResetGameUseCase(repository)

    @Provides
    fun provideGetGameUseCase(repository: GameRepository): GetGameUseCase = GetGameUseCase(repository)
}
