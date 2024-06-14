CREATE TABLE public.car
(
    id                  integer               NOT NULL,
    registration_number character varying(20) NOT NULL,
    personal_use        boolean               NOT NULL,
    PRIMARY KEY (id)
);