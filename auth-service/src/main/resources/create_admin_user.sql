-- Creates the very first platform admin if it doesn't exist
INSERT INTO users (email, password, name, company_name, role, created_at)
SELECT
    'platform-admin@yourcorp.io',
    -- bcrypt hash for string "StartupP@ss!"
    '$2a$10$PjZQfLWn54Pqtlcfz6OO4uw4zjO8sieEtzBVIzKXqofey5dpQTpeW',
    'Platform Admin',
    'YOUR-CORP',
    'GLOBAL_ADMIN',
    now()
    WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email = 'platform-admin@yourcorp.io'
);
