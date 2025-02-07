package org.intelehealth.app.ui.rosterquestionnaire.usecase

import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepository
import javax.inject.Inject

class GetHealthServiceQuestionUseCase @Inject constructor(private val rosterRepository: RosterRepository) {
    operator fun invoke(): ArrayList<RoasterViewQuestion> {
        return rosterRepository.getHealthServiceQuestionList()
    }
}