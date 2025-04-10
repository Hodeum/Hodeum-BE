package com.forzzle.hodeum.place.application;

import com.forzzle.hodeum.gemini.application.GeminiClient;
import com.forzzle.hodeum.gmap.application.GoogleMapClient;
import com.forzzle.hodeum.gmap.payload.dto.GooglePlacePreview;
import com.forzzle.hodeum.gmap.payload.dto.GooglePlacePreview.Place;
import com.forzzle.hodeum.gmap.payload.dto.PlaceDetail;
import com.forzzle.hodeum.gmap.payload.response.PlacePreviewResponse;
import com.forzzle.hodeum.place.payload.response.PlaceDetailResponse;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final GoogleMapClient googleMapClient;
    private final GeminiClient geminiClient;

    public List<PlacePreviewResponse> searchPlaces(String query) {

        GooglePlacePreview placePreviews = googleMapClient.searchPlaces(query);

        List<PlacePreviewResponse> response = makePlacePreviewResponses(placePreviews);

        return response;
    }

    private List<PlacePreviewResponse> makePlacePreviewResponses(GooglePlacePreview response) {
        List<Place> places = response.places();
        List<PlacePreviewResponse> result = new ArrayList<>();
        for (Place place : places) {
            String address = googleMapClient.searchAddressWithCoordinate(
                place.location().latitude(),
                place.location().longitude());
            result.add(
                new PlacePreviewResponse(
                    place.id(),
                    place.displayName().text(),
                    address,
                    place.primaryTypeDisplayName() != null ? place.primaryTypeDisplayName()
                        .text() : null
                )
            );
        }

        return result;
    }

    public PlaceDetailResponse getPlaceDetail(String placeId) {
        PlaceDetail placeDetail = googleMapClient.getPlaceDetail(placeId);
        String summary = geminiClient.getSummary(placeDetail.reviews());

        return new PlaceDetailResponse(placeDetail, summary);
    }
}
