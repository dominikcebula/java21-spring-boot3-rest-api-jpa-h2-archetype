#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web;

import ${package}.dto.MovieDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MoviesControllerIntegrationTest {
    @LocalServerPort
    private int localPort;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetMovies() {
        // when
        ResponseEntity<List<MovieDTO>> response = getAllMovies();

        // then
        assertSampleMoviesRetrieved(response);
    }

    @Test
    void shouldGetMovieById() {
        // given
        MovieDTO movieToRetrieve = GONE_WITH_THE_WIND;
        Long movieIdToRetrieve = movieToRetrieve.getId();

        // when
        ResponseEntity<MovieDTO> response = getMovieById(movieIdToRetrieve);

        //then
        assertMovieRetrieved(response, movieToRetrieve);
    }

    @Test
    void shouldReportNonExistingMovie() {
        // when
        ResponseEntity<MovieDTO> response = getMovieById(NON_EXISTING_MOVE_ID);

        //then
        assertMovieDidNotExist(response);
    }

    @Test
    @DirtiesContext
    void shouldCreateMovie() {
        // given
        var movieToCreate = new MovieDTO(GONE_WITH_THE_WIND.getTitle(), GONE_WITH_THE_WIND.getReleaseYear());

        // when
        ResponseEntity<MovieDTO> response = createMovie(movieToCreate);

        // then
        assertMovieCreated(response, movieToCreate);
    }

    @Test
    @DirtiesContext
    void shouldUpdateMovie() {
        // given
        var movieIdToUpdate = 2L;
        var movieDataToUpdate = new MovieDTO("2001: A Space Odyssey", 1968);
        var expectedMovieDataAfterUpdate = new MovieDTO(movieIdToUpdate, movieDataToUpdate.getTitle(), movieDataToUpdate.getReleaseYear());

        // when
        ResponseEntity<MovieDTO> responseBeforeUpdate = getMovieById(movieIdToUpdate);
        ResponseEntity<MovieDTO> updateResponse = updateMovieById(movieIdToUpdate, movieDataToUpdate);
        ResponseEntity<MovieDTO> responseAfterUpdate = getMovieById(movieIdToUpdate);

        // then
        assertMovieRetrieved(responseBeforeUpdate, CASABLANCA);
        assertMovieRetrieved(updateResponse, expectedMovieDataAfterUpdate);
        assertMovieRetrieved(responseAfterUpdate, expectedMovieDataAfterUpdate);
    }

    @Test
    @DirtiesContext
    void shouldDeleteMovie() {
        // given
        var movieToDelete = THE_SHAWSHANK_REDEMPTION.getId();

        // when
        ResponseEntity<Void> response = deleteMovieById(movieToDelete);

        // then
        assertMovieDeleted(response, movieToDelete);
    }

    private ResponseEntity<List<MovieDTO>> getAllMovies() {
        return restTemplate.exchange(
                getMoviesApiUrl(),
                HttpMethod.GET,
                new HttpEntity<>(Collections.emptyMap()),
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<MovieDTO> getMovieById(Long movieIdToRetrieve) {
        return restTemplate.getForEntity(getMovieIdUrl(movieIdToRetrieve), MovieDTO.class);
    }

    private ResponseEntity<MovieDTO> createMovie(MovieDTO movie) {
        return restTemplate.postForEntity(getMoviesApiUrl(), movie, MovieDTO.class);
    }

    private ResponseEntity<MovieDTO> updateMovieById(long movieIdToUpdate, MovieDTO movieDataToUpdate) {
        return restTemplate.exchange(
                getMovieIdUrl(movieIdToUpdate),
                HttpMethod.PUT,
                new HttpEntity<>(movieDataToUpdate),
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<Void> deleteMovieById(Long movieToDelete) {
        return restTemplate.exchange(
                getMovieIdUrl(movieToDelete),
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()), Void.class
        );
    }

    private String getMovieIdUrl(long id) {
        return getMoviesApiUrl() + "/" + id;
    }

    private String getMoviesApiUrl() {
        return getServerUrl("/api/movies");
    }

    private String getServerUrl(String endpoint) {
        return String.format("http://localhost:%d%s", localPort, endpoint);
    }

    private void assertSampleMoviesRetrieved(ResponseEntity<List<MovieDTO>> response) {
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .containsOnly(SAMPLE_MOVIES);
    }

    private void assertMovieRetrieved(ResponseEntity<MovieDTO> response, MovieDTO movieToRetrieve) {
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(movieToRetrieve);
    }

    private void assertMovieDidNotExist(ResponseEntity<MovieDTO> response) {
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNull();
    }

    private void assertMovieCreated(ResponseEntity<MovieDTO> response, MovieDTO movieToCreate) {
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders())
                .containsEntry(LOCATION, Collections.singletonList("/movies/6"));
        assertThat(response.getBody())
                .isNotNull();
        assertThat(response.getBody().getId())
                .isNotNull()
                .isNotZero();
        assertThat(response.getBody().getTitle())
                .isEqualTo(movieToCreate.getTitle());
        assertThat(response.getBody().getReleaseYear())
                .isEqualTo(movieToCreate.getReleaseYear());
    }

    private void assertMovieDeleted(ResponseEntity<Void> deletionResponse, Long movieToDelete) {
        assertThat(deletionResponse.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<MovieDTO> freshGetResponse = getMovieById(movieToDelete);
        assertThat(freshGetResponse.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    private static final long NON_EXISTING_MOVE_ID = 999L;
    private static final MovieDTO THE_GODFATHER = new MovieDTO(1L, "The Godfather", 1972);
    private static final MovieDTO CASABLANCA = new MovieDTO(2L, "Casablanca", 1942);
    private static final MovieDTO THE_SHAWSHANK_REDEMPTION = new MovieDTO(3L, "The Shawshank Redemption", 1994);
    private static final MovieDTO GONE_WITH_THE_WIND = new MovieDTO(4L, "Gone with the Wind", 1939);
    private static final MovieDTO PSYCHO = new MovieDTO(5L, "Psycho", 1960);
    private static final MovieDTO[] SAMPLE_MOVIES = {
            THE_GODFATHER,
            CASABLANCA,
            THE_SHAWSHANK_REDEMPTION,
            GONE_WITH_THE_WIND,
            PSYCHO
    };
}