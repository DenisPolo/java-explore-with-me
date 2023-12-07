package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static ru.practicum.constant.Constants.DATE_TIME_FORMATTER;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories", schema = "public")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "registration_date")
    private Instant registrationDate = Instant.now();

    @Override
    public String toString() {
        return "Category"
                + "{\"id\": " + id + ","
                + "\"name\": \"" + name + "\","
                + "\"registrationDate\": \"" + DATE_TIME_FORMATTER.format(registrationDate) + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id)
                && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (name == null) ? 0 : name.hashCode();
        return result;
    }
}