package com.haui.bookinghotel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewId implements Serializable {

    private Long userId;

    private Long hotelId;

    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(o == null) return false;
        if(getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(userId, reviewId.userId) &&
                Objects.equals(hotelId, reviewId.hotelId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(userId, hotelId);
    }

}
