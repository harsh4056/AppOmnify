package com.task.omnify.appomnify.Interfaces;

import java.util.ArrayList;

public interface TopStoriesListListener extends ResponseListener {
    void onStoriesRecieved(ArrayList<Long> stories);
}
