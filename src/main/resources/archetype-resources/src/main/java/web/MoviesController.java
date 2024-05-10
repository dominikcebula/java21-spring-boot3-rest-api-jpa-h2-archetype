#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web;

import ${package}.dto.MovieDTO;
import ${package}.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MoviesController {
    @Autowired
    private MoviesRepository moviesRepository;

    @GetMapping(path = "/movies")
    public List<MovieDTO> getMovies() {
        return moviesRepository.findAll();
    }

    @GetMapping(path = "/movies/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable("id") Long id) {
        return moviesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/movies")
    public ResponseEntity<MovieDTO> createMovie(@Validated @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = moviesRepository.save(movieDTO);
        return ResponseEntity.created(
                URI.create("/movies/" + createdMovie.getId())
        ).body(createdMovie);
    }

    @PutMapping(path = "/movies/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable("id") Long id, @Validated @RequestBody MovieDTO movieDTO) {
        if (!moviesRepository.existsById(id))
            return ResponseEntity.notFound().build();

        movieDTO.setId(id);

        MovieDTO updatedMovie = moviesRepository.save(movieDTO);

        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping(path = "/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable("id") Long id) {
        if (!moviesRepository.existsById(id))
            return ResponseEntity.notFound().build();

        moviesRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
