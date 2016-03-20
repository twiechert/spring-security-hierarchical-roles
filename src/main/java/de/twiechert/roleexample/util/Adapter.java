package de.twiechert.roleexample.util;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public interface Adapter <AdaptFrom, AdaptTo> {

    AdaptTo adapt(AdaptFrom adaptFrom);

}