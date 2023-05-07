package models;

import java.io.Serializable;
import java.util.Objects;

public class Tour implements Serializable {
    private int id;

    private String countryName;

    private String cityName;

    private float price;

    private String duration;

    private String tourCode;

    private String tourDate;

    private String tourName;

    private String tourType;

    public Tour() {
    }

    public Tour(String countryName, String cityName, float price, String duration,
                String tourCode, String tourDate, String tourName, String tourType) {
        this.countryName = countryName;
        this.cityName = cityName;
        this.price = price;
        this.duration = duration;
        this.tourCode = tourCode;
        this.tourDate = tourDate;
        this.tourName = tourName;
        this.tourType = tourType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTourCode() {
        return tourCode;
    }

    public void setTourCode(String tourCode) {
        this.tourCode = tourCode;
    }

    public String getTourDate() {
        return tourDate;
    }

    public void setTourDate(String tourDate) {
        this.tourDate = tourDate;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return Float.compare(tour.price, price) == 0
                && Objects.equals(countryName, tour.countryName)
                && Objects.equals(cityName, tour.cityName)
                && Objects.equals(duration, tour.duration)
                && Objects.equals(tourCode, tour.tourCode)
                && Objects.equals(tourDate, tour.tourDate)
                && Objects.equals(tourName, tour.tourName)
                && Objects.equals(tourType, tour.tourType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryName, cityName, price, duration, tourCode, tourDate, tourName, tourType);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", price=" + price +
                ", duration='" + duration + '\'' +
                ", tourCode='" + tourCode + '\'' +
                ", tourDate='" + tourDate + '\'' +
                ", tourName='" + tourName + '\'' +
                ", tourType='" + tourType + '\'' +
                '}';
    }
}
