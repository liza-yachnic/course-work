package models;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable {

    private int id;

    private String clientCode;

    private String tourCode;

    public Order() {
    }

    public Order(String clientCode, String tourCode) {
        this.clientCode = clientCode;
        this.tourCode = tourCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getTourCode() {
        return tourCode;
    }

    public void setTourCode(String tourCode) {
        this.tourCode = tourCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(clientCode, order.clientCode) && Objects.equals(tourCode, order.tourCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientCode, tourCode);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientCode='" + clientCode + '\'' +
                ", tourCode='" + tourCode + '\'' +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Order(clientCode, tourCode);
    }
}
