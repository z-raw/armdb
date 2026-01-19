-- Table: public.titles_v2

-- DROP TABLE IF EXISTS public.titles_v2;

CREATE TABLE IF NOT EXISTS public.titles_v2
(
    id uuid NOT NULL,
    tconst character varying(20) COLLATE pg_catalog."default",
    primary_title character varying(800) COLLATE pg_catalog."default",
    is_adult boolean,
    start_year integer,
    end_year integer,
    genres character varying(800) COLLATE pg_catalog."default",
    CONSTRAINT titles_v2_pkey PRIMARY KEY (id),
    CONSTRAINT titles_v2_tconst_key UNIQUE (tconst)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.titles_v2
    OWNER to postgres;

-- Table: public.casts

-- DROP TABLE IF EXISTS public.casts;

CREATE TABLE IF NOT EXISTS public.casts
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    nconst character varying(15) COLLATE pg_catalog."default" NOT NULL,
    primary_name character varying(800) COLLATE pg_catalog."default" NOT NULL,
    birth_year integer,
    death_year integer,
    primary_profession character varying(800) COLLATE pg_catalog."default",
    known_for_titles character varying(800) COLLATE pg_catalog."default",
    age smallint,
    is_alive boolean,
    CONSTRAINT casts_pkey PRIMARY KEY (id),
    CONSTRAINT casts_nconst_key UNIQUE (nconst)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.casts
    OWNER to postgres;
-- Index: idx_primary_name

-- DROP INDEX IF EXISTS public.idx_primary_name;

CREATE INDEX IF NOT EXISTS idx_primary_name
    ON public.casts USING btree
    (primary_name COLLATE pg_catalog."default" varchar_pattern_ops ASC NULLS LAST)
    WITH (deduplicate_items=True)
    TABLESPACE pg_default;

-- Index: idx_primary_name_gin

-- DROP INDEX IF EXISTS public.idx_primary_name_gin;

CREATE INDEX idx_primary_name_gin ON casts USING GIN (to_tsvector('english', primary_name));


-- Table: public.principals_v4 (previously titles_casts)

-- DROP TABLE IF EXISTS public.principals_v4;

CREATE TABLE IF NOT EXISTS public.principals_v4
(
    title_id uuid,
    tconst character varying(20) COLLATE pg_catalog."default",
    cast_id uuid,
    nconst character varying(20) COLLATE pg_catalog."default",
    characters character varying(40) COLLATE pg_catalog."default",
    CONSTRAINT fk_cast_id FOREIGN KEY (cast_id)
        REFERENCES public.casts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_title_id FOREIGN KEY (title_id)
        REFERENCES public.titles_v2 (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.principals_v4
    OWNER to postgres;