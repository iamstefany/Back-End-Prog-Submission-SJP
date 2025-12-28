--------------------- PRE-POPULATION SCRIPT ---------------------

-- Pre-populating the database with initial data for easier testing.
-- Data:
--   - 6 Users (Super Admin, Basic Admin, Guest User, Host User, Guest & Host, Blocked User)
--   - Corresponding Profiles for each user
--   - 20 Amenities
-- This script is run on each application startup, but uses ON CONFLICT DO NOTHING
-- to avoid duplicate entries if the data already exists.

-----------------------------------------------------------------

--------------------- Users ---------------------
INSERT INTO
users (user_id, email, password, first_name, last_name, is_blocked, profile_image_url, registration_date, roles)
VALUES
(
    '11111111-1111-1111-1111-111111111111', 'super.admin@example.com',
    '$2a$12$U9NzpfEMdVLjdYSU0KDqhO18t3n/YErEBXrgl8P2kMd2WieKR4BkS', -- Password: SuperAdmin123
    'Super', 'Admin', false, 'https://ui-avatars.com/api/?name=Super+Admin', '2026-01-01', 4
),
(
    '22222222-2222-2222-2222-222222222222', 'basic.admin@example.com',
    '$2a$12$FanxPh.YGq.Qd98jUJ23tursamLGKpJmq24rcA7fSjYznOFUBilem', -- Password: BasicAdmin123
    'Basic', 'Admin', false, 'https://ui-avatars.com/api/?name=Basic+Admin', '2026-01-01', 4
),
(
    '33333333-3333-3333-3333-333333333333', 'guest.user@example.com',
    '$2a$12$xzfvM80yPygDfejpVT1kKuwbV/HQEkqvWflfMiEKWRoSiXWEg.0.O', -- Password: GuestUser123
    'Guest', 'User', false, 'https://ui-avatars.com/api/?name=Guest+User', '2026-01-01', 1
),
(
    '44444444-4444-4444-4444-444444444444', 'host.user@example.com',
    '$2a$12$xMMlOf9m0bEhh9Q0JPzPKOztxbKNY0JrnXGpw5S3055QcW6qZ7a6q', -- Password: HostUser123
    'Host', 'User', false, 'https://ui-avatars.com/api/?name=Host+User', '2026-01-01', 2
),
(
    '55555555-5555-5555-5555-555555555555', 'guest.host@example.com',
    '$2a$12$I9V9HvlTz7E4fWzLTknTMeVPKP6oyOO/NIXb8KRFyqmCQLSLcMmby', -- Password: GuestHost123
    'Guest', '& Host', false, 'https://ui-avatars.com/api/?name=Guest+Host', '2026-01-01', 3
),
(
    '66666666-6666-6666-6666-666666666666', 'blocked.user@example.com',
    '$2a$12$QjCljn017vviTrCOovlsnOYuquaZxW5XPL.bvdQX56di7mkqAxZ9C', -- Password: BlockedUser123
    'Blocked', 'User', true, 'https://ui-avatars.com/api/?name=Blocked+User', '2026-01-01', 3
)
ON CONFLICT (user_id) DO NOTHING;


--------------------- Profiles ---------------------
INSERT INTO
admin_profiles (user_id, is_super_admin)
VALUES
('11111111-1111-1111-1111-111111111111', true),
('22222222-2222-2222-2222-222222222222', false)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO
guest_profiles (user_id, date_of_birth, phone_number)
VALUES
('33333333-3333-3333-3333-333333333333', '1990-05-15', '+1234567890'),
('55555555-5555-5555-5555-555555555555', '1985-10-20', '+0987654321'),
('66666666-6666-6666-6666-666666666666', '1995-12-25', '+1122334455')
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO
host_profiles (user_id, host_since, host_verified)
VALUES
('44444444-4444-4444-4444-444444444444', '2026-01-05', false),
('55555555-5555-5555-5555-555555555555', '2026-01-05', true),
('66666666-6666-6666-6666-666666666666', '2026-01-05', false)
ON CONFLICT (user_id) DO NOTHING;


--------------------- Amenities (20) ---------------------
INSERT INTO
amenities (amenity_id, name)
VALUES
('13cbacbf-8198-4406-984f-3baf2a2fb07b', 'Wi-Fi'),
('43bd47c6-8202-4d4d-aed9-0521be7af9ea', 'Air Conditioning'),
('efaa4779-7a6f-4c2d-836b-5a3bcd0d24d4', 'Heating'),
('9e167c7c-b5b9-4796-b973-ed53fbc07578', 'Kitchen'),
('902c993e-6979-4cd5-9ff8-a9f9a7d25781', 'Parking'),
('13537009-d406-4b27-80cf-e4099d21048c', 'Swimming Pool'),
('d6f42610-af3b-4409-b6dd-ccde196a20ae', 'Gym'),
('cb74f8de-6024-4f06-a62f-fe96fa3990cc', 'Washer'),
('0ab6b4ad-2fd6-41c0-a4a2-209408a56833', 'Dryer'),
('57409341-b572-4e57-9215-57ef9f59e6cc', 'TV'),
('fd05294b-8b5d-47f9-8ab8-acf88ce1bbbd', 'Breakfast'),
('c21fabcf-2a2a-4e72-a8fd-8256cce59b13', 'Pets Allowed'),
('1d52f565-d85b-4899-a07c-166f1aefcb02', 'Wheelchair Accessible'),
('30b5db79-3225-489d-9df0-bb3902b4392b', 'Fireplace'),
('1461547a-592d-4acc-9e11-f06642fad0d4', 'Balcony'),
('4693d14e-6e4c-4fbe-ba80-2e30f8731ddc', 'Garden'),
('8414cf55-2ae1-404b-bd2d-1a878069c327', 'Hot Tub'),
('c9325e70-e5be-4b59-ac21-7f33e0f94b43', 'Private Entrance'),
('f2288bf3-f9e1-40c1-9ffb-8a0086acfb95', 'Elevator'),
('70e0c701-418e-42c3-93cd-f140f1da8322', 'Smoking Allowed')
ON CONFLICT (amenity_id) DO NOTHING;