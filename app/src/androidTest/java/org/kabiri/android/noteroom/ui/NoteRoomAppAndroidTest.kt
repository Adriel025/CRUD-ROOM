package org.kabiri.android.noteroom.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.kabiri.android.noteroom.*
import org.kabiri.android.noteroom.model.NoteEntity
import org.kabiri.android.noteroom.viewmodel.HomeViewModelAbstract

class NoteRoomAppAndroidTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeSelectedNoteState = mutableStateOf<NoteEntity?>(null)
    private val fakeNoteList = mutableListOf<NoteEntity>()
    private val mockViewModel = object: HomeViewModelAbstract {
        override val selectedNoteState: State<NoteEntity?> = fakeSelectedNoteState
        override val noteListFlow: Flow<List<NoteEntity>> = flowOf(fakeNoteList)
        override fun addOrUpdateNote(note: NoteEntity) {
            fakeNoteList.clear()
            fakeNoteList.add(note) }
        override fun deleteNote(note: NoteEntity) { fakeNoteList.remove(note) }
        override fun selectNote(note: NoteEntity) { fakeSelectedNoteState.value = note }
        override fun resetSelectedNote() { fakeSelectedNoteState.value = null }
    }

    @Before
    fun setUp() {
        fakeSelectedNoteState.value = null
        fakeNoteList.clear()
    }

    @Test
    fun testComposeNodesAppearsCorrectly() {

        composeTestRule.setContent {
            NoteRoomApp(homeViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag(HOME_BUTTON_NOTE_ADD).assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag(NOTE_TEXT_FIELD).assertExists()
        composeTestRule.onNodeWithTag(NOTE_BUTTON_DONE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NOTE_BUTTON_BACK).assertIsDisplayed()
    }

    @Test
    fun testAddingAndUpdatingANoteWorksCorrectly() {

        val fakeNote = NoteEntity(roomId = 123, text = "doesn't matter")

        composeTestRule.setContent {
            NoteRoomApp(homeViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag(HOME_BUTTON_NOTE_ADD).performClick()

        composeTestRule.onNodeWithTag(NOTE_TEXT_FIELD).performClick()
            .performTextInput(fakeNote.text)
        composeTestRule.onNodeWithTag(NOTE_BUTTON_DONE).performClick()

        composeTestRule.onAllNodesWithText(fakeNote.text).assertCountEquals(1)
            .onFirst().performClick()

        composeTestRule.onNodeWithText(fakeNote.text).performTextInput("-updated")
        composeTestRule.onNodeWithTag(NOTE_BUTTON_DONE).performClick()

        composeTestRule.onNodeWithText(fakeNoteList.first().text).assertIsDisplayed()
    }

    @Test
    fun testSwipingANoteDeletesItCorrectly() {

        val fakeNote = NoteEntity(roomId = 123, text = "doesn't matter")

        composeTestRule.setContent {
            NoteRoomApp(homeViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag(HOME_BUTTON_NOTE_ADD).performClick()
        composeTestRule.onNodeWithTag(NOTE_TEXT_FIELD).performClick()
            .performTextInput(fakeNote.text)
        composeTestRule.onNodeWithTag(NOTE_BUTTON_DONE).performClick()

        composeTestRule.onNodeWithText(fakeNote.text).performTouchInput { swipeLeft() }

        assertThat(fakeNoteList.size).isEqualTo(0)
    }
}