package org.intelehealth.app.ui.rosterquestionnaire.usecase

import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepository
import javax.inject.Inject

class GetOutComeQuestionUseCase @Inject constructor(private val repository: RosterRepository) {
    operator fun invoke(existingRoasterQuestionList: ArrayList<RoasterViewQuestion>?): ArrayList<RoasterViewQuestion> {
        return if (existingRoasterQuestionList.isNullOrEmpty()) {
            repository.getOutcomeQuestionList()
        } else {
            existingRoasterQuestionList
        }
    }
}