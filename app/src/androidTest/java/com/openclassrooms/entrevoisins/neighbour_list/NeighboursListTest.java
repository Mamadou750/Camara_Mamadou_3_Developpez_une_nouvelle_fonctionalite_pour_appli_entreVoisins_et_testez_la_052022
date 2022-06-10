package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {


    private static int ITEMS_COUNT;
    private final int POSITION_ITEM = 0;

    private ListNeighbourActivity mActivity;
    private NeighbourApiService mService;
    private List<Neighbour> neighbourList;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);


    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mService = DI.getNewInstanceApiService();
        neighbourList = mService.getNeighbours();
        ITEMS_COUNT = neighbourList.size();
    }



    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position selected
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        System.out.println("Nombre d'objet " + ITEMS_COUNT);
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
        System.out.println("Nombre d'objet:" + ITEMS_COUNT);
    }

    /**
     * Open ProfilNeighbour, when click on list element.
     */
    @Test
    public void myNeighboursList_onClickItem_shouldOpenProfilNeighbour() {
        //Result : Launch page profile
        //Item click
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //After : Verification of the display of the First name.
        onView(withId(R.id.profile_name)).check(matches(isDisplayed()));
    }

    /**
     * Check if the name in ProfilNeighbour is the same as the item selected.
     */
    @Test
    public void neighbourName_onProfilNeighbour_isCorrect() {
        Neighbour neighbour = neighbourList.get(POSITION_ITEM);

        //result : the right name on profile
        //when : profile opening
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //after: Verification if the first name of the profile corresponds to the first name of the neighbor.
        onView(withId(R.id.profile_name)).check(matches(withText(neighbour.getName())));
    }

    /**
     * Check if favorite list contain items marked as favorite.
     */
    @Test
    public void favoritesList_onFavoriteTab_showFavoriteItems() {
        //Result: List of favorites in the favorites tab.
        // slide to favorite
        onView(withId(R.id.container)).perform(swipeLeft());

        //check if favorite is empty
        onView(ViewMatchers.withId(R.id.fav_neighbours)).check(withItemCount(0));

        //slide to neighbour-list
        onView(withId(R.id.container)).perform(swipeRight());

        //when: Add 1 favorites using the fab button.
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        onView(withId(R.id.profile_favorite))
                .perform(click());
        pressBack();

        //slides to the favorites tab.
        onView(withId(R.id.container)).perform(swipeLeft());

        //after: Verification of the number of favorites to add (=1).
        onView(ViewMatchers.withId(R.id.fav_neighbours)).check(withItemCount(1));
    }

    /**
     * When we delete an item in favorite, the item is no more shown
     */
    @Test
    public void myNeighboursListFavorite_deleteAction_shouldRemoveItemFromFavorite() {
        // Result: Removal of a favourite.
        // slide to favorite
        onView(withId(R.id.container)).perform(swipeLeft());

        //check if favorite is empty
        onView(ViewMatchers.withId(R.id.fav_neighbours)).check(withItemCount(0));

        //slide to neighbour-list
        onView(withId(R.id.container)).perform(swipeRight());


        //when: Add 1 favorites using the fab button.
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        onView(withId(R.id.profile_favorite))
                .perform(click());
        pressBack();
        onView(withId(R.id.container)).perform(swipeLeft());

        //check that the favorites list is not empty.
        onView(ViewMatchers.withId(R.id.fav_neighbours)).check(withItemCount(1));

        // click on the fav neighbour
        onView(ViewMatchers.withId(R.id.fav_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.profile_favorite))
                .perform(click());
        pressBack();
        // after: checks the number of items in the favorites list is -1
        onView(ViewMatchers.withId(R.id.fav_neighbours)).check(withItemCount(0));
    }
}