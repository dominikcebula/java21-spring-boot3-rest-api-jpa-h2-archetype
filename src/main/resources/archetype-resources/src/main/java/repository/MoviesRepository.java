#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.repository;

import ${package}.dto.MovieDTO;
import org.springframework.data.repository.ListCrudRepository;

public interface MoviesRepository extends ListCrudRepository<MovieDTO, Long> {
}
