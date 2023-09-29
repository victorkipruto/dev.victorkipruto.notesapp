package dev.victorkipruto.mynotes.feature_note.presentation.notes_screen.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.victorkipruto.mynotes.core.util.TestTags
import dev.victorkipruto.mynotes.di.AppModule
import dev.victorkipruto.mynotes.feature_note.presentation.MainActivity
import dev.victorkipruto.mynotes.feature_note.presentation.util.Screen
import dev.victorkipruto.mynotes.ui.theme.MyNotesTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest
{
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp()
    {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController:NavHostController = rememberNavController()
            MyNotesTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                )
                {
                    composable(route=Screen.NotesScreen.route)
                    {
                        NotesScreen(navController = navController)
                    }
                }
            }
        }
    }


    @Test
    fun click_Toggle_OrderSection_is_Visible()
    {
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertIsDisplayed()
        }

}