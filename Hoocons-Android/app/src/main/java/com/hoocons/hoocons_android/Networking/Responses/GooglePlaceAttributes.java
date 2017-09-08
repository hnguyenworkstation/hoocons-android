package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 9/8/17.
 */

public class GooglePlaceAttributes {
    @SerializedName("address_components")
    private List<AddressComponents> addressComponents;
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("geometry")
    private GooglePlaceGeometry geometry;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("types")
    private List<String> types;

    public class AddressComponents {
        @SerializedName("long_name")
        private String longName;
        @SerializedName("short_name")
        private String shortName;
        @SerializedName("types")
        private List<String> types;

        public AddressComponents(String longName, String shortName, List<String> types) {
            this.longName = longName;
            this.shortName = shortName;
            this.types = types;
        }

        public String getLongName() {
            return longName;
        }

        public void setLongName(String longName) {
            this.longName = longName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }

    public List<AddressComponents> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponents> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public GooglePlaceGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GooglePlaceGeometry geometry) {
        this.geometry = geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
