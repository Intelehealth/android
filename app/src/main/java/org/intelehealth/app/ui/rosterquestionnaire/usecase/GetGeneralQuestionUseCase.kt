package org.intelehealth.app.ui.rosterquestionnaire.usecase

import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepository
import javax.inject.Inject

class GetGeneralQuestionUseCase @Inject constructor(private val rosterRepository: RosterRepository) {
    operator fun invoke(value: ArrayList<RoasterViewQuestion>?): ArrayList<RoasterViewQuestion> =
        if (value.isNullOrEmpty()) {
            rosterRepository.getGeneralQuestionList()
        } else {
            value
        }
}