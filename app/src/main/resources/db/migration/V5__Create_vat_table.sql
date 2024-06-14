CREATE TABLE public.vat
(
    id bigserial NOT NULL,
    name character varying(20) NOT NULL,
    rate numeric(3, 2) NOT NULL,
    PRIMARY KEY (id)
);

insert into public.vat (name, rate)
values ('23', 0.23),
       ('8', 0.08),
       ('5', 0.05),
       ('0', 0.01),
       ('ZW', 0.01);

