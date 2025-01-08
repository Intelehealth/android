package org.intelehealth.app.ui.rosterquestionnaire.usecase

import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepository
import javax.inject.Inject

class GetHealthServiceQuestionUseCase @Inject constructor(private val rosterRepository: RosterRepository) {
   operator fun invoke(existingRoasterQuestionList: ArrayList<RoasterViewQuestion>?): ArrayList<RoasterViewQuestion> {
        return if (existingRoasterQuestionList.isNullOrEmpty()) {
            rosterRepository.getHealthServiceQuestionList()
        } else {
            existingRoasterQuestionList
        }
    }
}