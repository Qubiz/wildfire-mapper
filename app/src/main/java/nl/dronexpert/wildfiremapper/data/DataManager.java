package nl.dronexpert.wildfiremapper.data;

import nl.dronexpert.wildfiremapper.data.database.DatabaseHelper;
import nl.dronexpert.wildfiremapper.data.network.ApiHelper;
import nl.dronexpert.wildfiremapper.data.preferences.PreferencesHelper;

public interface DataManager extends DatabaseHelper, PreferencesHelper, ApiHelper {
    //TODO Implement ways to interact with the application's data manager
}
