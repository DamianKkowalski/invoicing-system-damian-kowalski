CREATE TABLE public.invoice_invoice_entry
(
    invoice_id       integer NOT NULL,
    invoice_entry_id integer NOT NULL,
    PRIMARY KEY (invoice_id, invoice_entry_id)
);

ALTER TABLE public.invoice_invoice_entry
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id)
        REFERENCES public.invoice (id)
        ON DELETE CASCADE;

ALTER TABLE public.invoice_invoice_entry
    ADD CONSTRAINT invoice_entry_id_fk FOREIGN KEY (invoice_entry_id)
        REFERENCES public.invoice_entry (id)
        ON DELETE CASCADE;