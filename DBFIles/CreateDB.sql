DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.favourite_movies CASCADE;
DROP TABLE IF EXISTS public.movies CASCADE;
DROP TABLE IF EXISTS public.rate_movies CASCADE;
DROP TABLE IF EXISTS public.comment_movies CASCADE;

DROP TYPE IF EXISTS RATING_SEPFLIX;

CREATE DOMAIN rating_sepflix AS DECIMAL
  CHECK (
    VALUE <= 10
    AND VALUE >= 0
  );

CREATE TABLE public.users
(
  user_name VARCHAR(25) PRIMARY KEY NOT NULL,
  name      VARCHAR(25)             NOT NULL,
  surname   VARCHAR(25)             NOT NULL,
  email     VARCHAR(50)             NOT NULL,
  password  VARCHAR(255)            NOT NULL
);
CREATE UNIQUE INDEX users_email_uindex
  ON public.users (email);

CREATE TABLE public.movies (
  id_movie       INT PRIMARY KEY       NOT NULL,
  poster         VARCHAR(100)          NOT NULL,
  title          VARCHAR(100)          NOT NULL,
  genres         VARCHAR(100)          NOT NULL,
  overview       VARCHAR(400)          NOT NULL,
  release_year   DATE                  NOT NULL,
  rating_imdb    DECIMAL               NOT NULL,
  rating_sepflix DECIMAL DEFAULT NULL  NULL
);

CREATE TABLE public.favourite_movies (
  id_movie  INT         NOT NULL
    REFERENCES public.movies (id_movie)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  user_name VARCHAR(25) NOT NULL
    REFERENCES public.users (user_name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  PRIMARY KEY (id_movie, user_name)
);

CREATE TABLE public.rate_movies (
  id_movie     INT            NOT NULL
    REFERENCES public.movies (id_movie)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  user_name    VARCHAR(25)    NOT NULL
    REFERENCES public.users (user_name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  rate_sepflix RATING_SEPFLIX NOT NULL,
  PRIMARY KEY (id_movie, user_name)
);

CREATE TABLE public.comment_movies (
  id_movie  INT         NOT NULL
    REFERENCES public.movies (id_movie)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  user_name VARCHAR(25) NOT NULL
    REFERENCES public.users (user_name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  comment   VARCHAR(255)
);

CREATE OR REPLACE FUNCTION process_average_vote_count()
  RETURNS TRIGGER AS $vote_average_count$
BEGIN
  IF (TG_OP = 'DELETE')
  THEN
    UPDATE movies
    SET rating_sepflix = (SELECT AVG(rate_sepflix)
                          FROM rate_movies
                          WHERE rate_movies.id_movie = old.id_movie)
    WHERE id_movie = old.id_movie;
    RETURN old;
  ELSE
    UPDATE movies
    SET rating_sepflix = (SELECT AVG(rate_sepflix)
                          FROM rate_movies
                          WHERE rate_movies.id_movie = new.id_movie)
    WHERE id_movie = new.id_movie;
    RETURN new;
  END IF;

END;
$vote_average_count$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_average_vote_count
AFTER INSERT OR UPDATE OR DELETE ON public.rate_movies
FOR EACH ROW
EXECUTE PROCEDURE process_average_vote_count();
