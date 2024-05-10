#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class MovieDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String title;
    @Column(nullable = false)
    @NotNull
    private int releaseYear;

    public MovieDTO(String title, int releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }
}
