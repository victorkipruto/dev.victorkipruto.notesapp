package dev.victorkipruto.mynotes.feature_note.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.victorkipruto.mynotes.core.util.TestTags
import dev.victorkipruto.mynotes.di.AppModule
import dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen.components.AddEditNoteScreen
import dev.victorkipruto.mynotes.feature_note.presentation.notes_screen.components.NotesScreen
import dev.victorkipruto.mynotes.feature_note.presentation.util.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest
{
    @get:Rule(order=0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order=1)
    val composeRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp()
    {
        hiltRule.inject()
        composeRule.activity.setContent{
            val navController:NavHostController = rememberNavController()

            NavHost(navController = navController, startDestination =Screen.NotesScreen.route )
            {
                composable(route = Screen.NotesScreen.route)
                {
                    NotesScreen(navController = navController)
                }
                composable(route = Screen.AddEditNotesScreen.route +
                        "?noteId={noteId}&noteColor={noteColor}",
                    arguments = listOf(
                        navArgument(name = "noteId")
                        {
                            type= NavType.IntType
                            defaultValue = -1
                        },
                        navArgument(name="noteColor")
                        {
                            type= NavType.IntType
                            defaultValue = -1
                        }
                    )
                )
                {
                    val color = it.arguments?.getInt("noteColor") ?: -1
                    AddEditNoteScreen(navController = navController, noteColor = color)
                }
            }
        }
    }



    @Test
    fun saveNewNote_editAfterwards()
    {
        composeRule.onNodeWithContentDescription("New note").performClick()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("Test title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).performTextInput("Test content")
        composeRule.onNodeWithContentDescription("Save note").performClick()

        composeRule.onNodeWithText("Test title").assertIsDisplayed()

        composeRule.onNodeWithText("Test title").performClick()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("Test title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).assertTextEquals("Test content")

        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextClearance()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("Test title 2")
        composeRule.onNodeWithContentDescription("Save note").performClick()

        composeRule.onNodeWithText("Test title 2").assertIsDisplayed()




    }
}