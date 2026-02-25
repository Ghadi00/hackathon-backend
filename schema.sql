CREATE TABLE surveys (
  id           TEXT PRIMARY KEY,
  slug         TEXT UNIQUE NOT NULL,
  title        TEXT NOT NULL,
  is_active    BOOLEAN DEFAULT TRUE,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  published_at TIMESTAMPTZ,
  expires_at   TIMESTAMPTZ
);

-- ============================================================
-- LEARNERS
-- Keyed by email — one row per unique person.
-- PII is isolated here; geo/utm live on the response.
-- ============================================================
CREATE TABLE learners (
  email      TEXT PRIMARY KEY,
  full_name  TEXT,
  phone      TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- RESPONSES
-- One response per learner per survey (enforced by UNIQUE).
-- Holds submission metadata — not profile answers.
-- ============================================================
CREATE TABLE responses (
  id            TEXT PRIMARY KEY,              -- responses[].id from API
  survey_id     TEXT NOT NULL REFERENCES surveys(id),
  learner_email TEXT NOT NULL REFERENCES learners(email),

  ip_address   TEXT,
  geo_country  TEXT,
  geo_region   TEXT,
  geo_city     TEXT,
  utm_source   TEXT,
  utm_medium   TEXT,
  utm_campaign TEXT,

  submitted_at TIMESTAMPTZ NOT NULL,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE (survey_id, learner_email)
);

-- ============================================================
-- RESPONSE PROFILES
-- Structured survey answers, keyed by response_id.
-- One row per response.
-- Powers → GET /profiles/demographics  GET /profiles/programs
-- ============================================================
CREATE TABLE response_profiles (
  response_id TEXT PRIMARY KEY REFERENCES responses(id) ON DELETE CASCADE,

  -- Demographics
  age_range         TEXT,   -- '18_24', '25_34', '35_44', ...
  employment_status TEXT,   -- 'student', 'full_time', 'freelancer', ...
  job_level         TEXT,   -- 'manager', 'specialist', 'student', ...
  other_job_level   TEXT,   -- free-text when job_level = 'other'
  experience_years  TEXT,   -- '0_1', '2_5', '6_10', '10_plus', 'na'

  -- Program selection
  training_track     TEXT NOT NULL, -- 'microsoft_ai_academy', 'lebanon_coding', ...
  access_channel     TEXT NOT NULL, -- 'university', 'employer', 'ngo', 'public_sector', 'other'
  access_entity_name TEXT,          -- resolved from university_name / ngo_name / employer_name / etc.

  -- Self-assessed skill levels
  digital_literacy_level TEXT,   -- 'Advanced', 'Intermediate', 'Basic', 'None'
  cybersecurity_level    TEXT,
  ai_programming_level   TEXT,
  data_skills_level      TEXT,

  -- Consents
  data_processing_consent BOOLEAN DEFAULT FALSE,
  follow_up_consent       BOOLEAN DEFAULT FALSE
);

-- ============================================================
-- RESPONSE LEARNING REASONS (multi-select)
-- Normalised from responses[].responses.learning_reason[]
-- Powers → GET /interests/motivations
-- ============================================================
CREATE TABLE response_learning_reasons (
  id          BIGSERIAL PRIMARY KEY,
  response_id TEXT NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
  reason      TEXT NOT NULL,  -- 'personal', 'career_growth', 'job_transition', 'business'
  UNIQUE (response_id, reason)
);

-- ============================================================
-- RESPONSE AI GOALS (multi-select)
-- Normalised from responses[].responses.ai_goals[]
-- Powers → GET /interests/areas  GET /interests/motivations
-- ============================================================
CREATE TABLE response_ai_goals (
  id          BIGSERIAL PRIMARY KEY,
  response_id TEXT NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
  goal        TEXT NOT NULL,  -- 'productivity', 'career', 'business_ai', 'build_tools', 'strategy', 'explore'
  UNIQUE (response_id, goal)
);

-- ============================================================
-- PROVIDER STATUS (secondary source: Microsoft / Oracle)
-- Powers the badge on the unified learner profile.
-- ============================================================
--CREATE TABLE response_provider_status (
--  id              BIGSERIAL PRIMARY KEY,
--  response_id     TEXT UNIQUE NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
--  provider        TEXT NOT NULL,    -- 'microsoft', 'oracle'
--  external_ref    TEXT,             -- student ID on provider platform
--  progress_pct    NUMERIC(5,2),     -- 0.00 – 100.00
--  is_completed    BOOLEAN DEFAULT FALSE,
--  certificate_url TEXT,
--  synced_at       TIMESTAMPTZ
--);

-- ============================================================
-- INDEXES
-- ============================================================

-- Dissemination performance
CREATE INDEX idx_rp_channel      ON response_profiles (access_channel);
CREATE INDEX idx_rp_entity       ON response_profiles (access_entity_name);
CREATE INDEX idx_rp_track        ON response_profiles (training_track);

-- Geographic insights
CREATE INDEX idx_resp_region     ON responses (geo_region);
CREATE INDEX idx_resp_city       ON responses (geo_city);
CREATE INDEX idx_resp_country    ON responses (geo_country);

-- Growth over time
CREATE INDEX idx_resp_submitted  ON responses (submitted_at);

-- Channel + region cross-analysis
CREATE INDEX idx_rp_channel_resp ON response_profiles (access_channel, response_id);

-- Interest & motivation lookups
CREATE INDEX idx_reasons_reason  ON response_learning_reasons (reason);
CREATE INDEX idx_goals_goal      ON response_ai_goals (goal);

-- UTM tracking
CREATE INDEX idx_resp_utm_source ON responses (utm_source);