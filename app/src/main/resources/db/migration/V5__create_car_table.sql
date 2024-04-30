CREATE TABLE public.car
(
    id bigserial NOT NULL,
    registration_number character varying(20) NOT NULL,
    personal_user character varying(100) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.car
    OWNER to postgres;