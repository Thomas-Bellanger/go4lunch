package com.example.go4lunch;

import static org.junit.Assert.assertFalse;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.service.ApiService;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    public User fakeUser1 = new User("fakeUID", "fakeUser1", "https://www.gravatar.com/avatar/HASH", Restaurant.restaurant1, "fakeMail.com");
    public User fakeUser2 = new User("fakeUID2", "fakeUser2", "https://www.gravatar.com/avatar/HASH", Restaurant.restaurant2, "fakeMail2.com");
    public Restaurant restaurant1 = new Restaurant("1863", "restaurant 1", "8 Rue des restaurants", "French", true, "0.0", "https://i.pravatar.cc/150?u=a042581f4e29026704d", "https://i.pravatar.cc/150?u=a042581f4e29026704d", 3418, 10, 48.8675, 2.6901);
    public Restaurant restaurant2 = new Restaurant("2854", "restaurant 2", "10 Rue des restaurants", "French", true,"0.0" , "https://i.pravatar.cc/150?u=a042581f4e29026704d", "https://i.pravatar.cc/150?u=a042581f4e29026704d", 3418, 5, 48.8341, 2.7958);


    @Test
    public void addFavoriteIsSuccessful(){
        fakeUser1.addFavorite(restaurant1);
        assert (fakeUser1.getFavorite().contains(restaurant1));
    }

    @Test
    public void removeFavoriteIsSuccessful(){
        fakeUser1.addFavorite(restaurant1);
        fakeUser1.removeFavorite(restaurant1);
        assert (fakeUser1.getFavorite().isEmpty());
    }

    @Test
    public void addJoinerIsSuccessful(){
        restaurant1.addJoiners(fakeUser1);
        assert (restaurant1.getJoiners().contains(fakeUser1));
    }

    @Test
    public void removeJoinerIsSuccessful(){
        restaurant1.addJoiners(fakeUser1);
        restaurant1.addJoiners(fakeUser2);
        assert (restaurant1.getJoiners().contains(fakeUser1));
        restaurant1.removeJoiners(fakeUser1);
        assertFalse(restaurant1.getJoiners().contains(fakeUser1));
    }

    @Test
    public void updateLunchWithSuccess(){
        fakeUser1.setChosenRestaurant(restaurant2);
        assert (fakeUser1.getChosenRestaurant() == restaurant2);
    }
}