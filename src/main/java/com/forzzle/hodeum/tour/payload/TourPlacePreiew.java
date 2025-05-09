package com.forzzle.hodeum.tour.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.forzzle.hodeum.tour.util.ItemsDeserializer;
import java.util.List;

public record TourPlacePreiew(Response response) {

    public record Response(Body body) {

        public record Body(Items items, int numOfRows, int pageNo, int totalCount) {

            @JsonDeserialize(using = ItemsDeserializer.class)
            public record Items(List<Item> item) {

                public record Item(String contentid, String title) {

                }
            }
        }
    }

    public String getContentId() {
        if (isEmpty()) {
            return null;
        }
        return response.body.items.item.get(0).contentid;
    }

    public String getTitle() {
        if (isEmpty()) {
            return null;
        }
        return response.body.items.item.get(0).title;
    }

    public boolean isEmpty() {
        return response.body.totalCount == 0;
    }

}
