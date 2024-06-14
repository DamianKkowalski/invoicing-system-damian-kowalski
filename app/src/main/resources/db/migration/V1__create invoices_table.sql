CREATE TABLE public.invoice (
    id         integer                   NOT NULL,
    date       date                        NOT NULL,
    number     character varying(50)       NOT NULL,
    PRIMARY KEY (id)
);