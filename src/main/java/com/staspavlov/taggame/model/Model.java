package com.staspavlov.taggame.model;

import com.staspavlov.taggame.game.Game;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Model.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public class Model {

    private final ObjectProperty<Game> gameProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Locale> localeProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ElementSize> elementSizeProperty = new SimpleObjectProperty<>();

    public Model(Game game, Locale locale, ElementSize elementSize) {
        gameProperty.setValue(game);
        localeProperty.setValue(locale);
        elementSizeProperty.setValue(elementSize);
    }

    /**
     * Returns game property.
     * @return Game property
     */
    public ObjectProperty<Game> getGameProperty() {
        return gameProperty;
    }

    /**
     * Returns game.
     * @return Game
     */
    public Game getGame() {
        return gameProperty.getValue();
    }

    /**
     * Sets game.
     * @param game Game
     */
    public void setGame(Game game) {
        gameProperty.setValue(game);
    }

    /**
     * Returns locale property.
     * @return Locale property
     */
    public ObjectProperty<Locale> getLocaleProperty() {
        return localeProperty;
    }

    /**
     * Returns locale.
     * @return Locale
     */
    public Locale getLocale() {
        return localeProperty.getValue();
    }

    /**
     * Sets locale.
     * @param locale Locale
     */
    public void setLocale(Locale locale) {
        localeProperty.setValue(locale);
    }

    /**
     * Returns element size property.
     * @return Element size property
     */
    public ObjectProperty<ElementSize> getElementSizeProperty() {
        return elementSizeProperty;
    }

    /**
     * Returns element size.
     * @return Element size
     */
    public ElementSize getElementSize() {
        return elementSizeProperty.getValue();
    }

    /**
     * Sets element size.
     * @param elementSize Element size
     */
    public void setElementSize(ElementSize elementSize) {
        elementSizeProperty.setValue(elementSize);
    }

}
