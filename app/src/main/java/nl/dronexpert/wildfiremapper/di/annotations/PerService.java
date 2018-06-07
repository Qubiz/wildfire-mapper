package nl.dronexpert.wildfiremapper.di.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Mathijs de Groot on 07/06/2018.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
