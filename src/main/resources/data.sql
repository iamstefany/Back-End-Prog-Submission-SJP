--------------------- PRE-POPULATION SCRIPT ---------------------

-- Pre-populating the database with initial data for easier testing.
-- Data:
--   - 6 Users (Super Admin, Basic Admin, Guest User, Host User, Guest & Host User, Blocked User)
--   - Corresponding Profiles for each user
--   - 20 Amenities
--   - 7 Properties (and relevant images and amenities associations)
-- This script is run on each application startup, but uses ON CONFLICT DO NOTHING
-- to avoid duplicate entries if the data already exists.

-----------------------------------------------------------------

--------------------- Users (password is always Password123) ---------------------
INSERT INTO
users (user_id, email, password, first_name, last_name, is_blocked, profile_image_url, registration_date, roles)
VALUES
(
    '11111111-1111-1111-1111-111111111111', 'super.admin@example.com',
    '$2a$12$1NbZcLDEG.bKRpTMRG4nRu3HqKN/CHWK8RTQFPnd8uEmNFzuEMFkG',
    'Super', 'Admin', false, 'https://ui-avatars.com/api/?name=Super+Admin', '2026-01-01', 4
),
(
    '22222222-2222-2222-2222-222222222222', 'basic.admin@example.com',
    '$2a$12$1NbZcLDEG.bKRpTMRG4nRu3HqKN/CHWK8RTQFPnd8uEmNFzuEMFkG',
    'Basic', 'Admin', false, 'https://ui-avatars.com/api/?name=Basic+Admin', '2026-01-01', 4
),
(
    '33333333-3333-3333-3333-333333333333', 'guest.user@example.com',
    '$2a$12$1NbZcLDEG.bKRpTMRG4nRu3HqKN/CHWK8RTQFPnd8uEmNFzuEMFkG',
    'Guest', 'User', false, 'https://ui-avatars.com/api/?name=Guest+User', '2026-01-01', 1
),
(
    '44444444-4444-4444-4444-444444444444', 'host.user@example.com',
    '$2a$12$1NbZcLDEG.bKRpTMRG4nRu3HqKN/CHWK8RTQFPnd8uEmNFzuEMFkG',
    'Host', 'User', false, 'https://ui-avatars.com/api/?name=Host+User', '2026-01-01', 2
),
(
    '55555555-5555-5555-5555-555555555555', 'guest.host@example.com',
    '$2a$12$1NbZcLDEG.bKRpTMRG4nRu3HqKN/CHWK8RTQFPnd8uEmNFzuEMFkG',
    'Guest', '& Host', false, 'https://ui-avatars.com/api/?name=Guest+Host', '2026-01-01', 3
),
(
    '66666666-6666-6666-6666-666666666666', 'blocked.user@example.com',
    '$2a$12$1NbZcLDEG.bKRpTMRG4nRu3HqKN/CHWK8RTQFPnd8uEmNFzuEMFkG',
    'Blocked', 'User', true, 'https://ui-avatars.com/api/?name=Blocked+User', '2026-01-01', 3
)
ON CONFLICT (user_id) DO NOTHING;


---- Profiles
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
('44444444-4444-4444-4444-444444444444', '2026-01-05', true),
('55555555-5555-5555-5555-555555555555', '2026-01-05', true),
('66666666-6666-6666-6666-666666666666', '2026-01-05', false)
ON CONFLICT (user_id) DO NOTHING;
-----------------------------------------------------------------

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
-----------------------------------------------------------------

--------------------- Properties (7) ---------------------
INSERT INTO properties
(property_id, address, automatic_confirmation, check_in_time, check_out_time, city, country, description, max_guests, price_per_night, title, user_id)
VALUES
('8164dd55-a730-4a84-a376-4c6f697aeadb', 'Via Bologna 1', true, '15:00:00', '10:00:00', 'Torino', 'IT', 'This beautiful property will make you feel right at home.', 2, 60.00, 'Beautiful property in city center', '44444444-4444-4444-4444-444444444444'),
('fb53eed4-b6c3-467f-b89a-e0babaaa92e0', 'Via Montebello', true, '15:00:00', '10:00:00', 'Torino', 'IT', 'Prestigious location, we look forward to welcoming you!', 3, 90.00, 'Amazing property near Mole Antoneliana', '44444444-4444-4444-4444-444444444444'),
('191b86ed-c9f9-4713-90e3-167aba489668', 'Piazza del Duomo', true, '15:00:00', '10:00:00', 'Milano', 'IT', 'This property is a 5-minutes walk away from Duomo!', 2, 150.00, 'Property near Duomo', '44444444-4444-4444-4444-444444444444'),
('e9f803e2-89b1-40bc-8c9f-60e0dca8d953', 'Via Borgogna', true, '15:00:00', '10:00:00', 'Milano', 'IT', 'Close to San Babila, perfect for families.', 5, 190.00, 'Lovely property in Milan', '44444444-4444-4444-4444-444444444444'),
('426d603e-3af4-431e-a7c7-ebdd1db1b83a', 'Piazza del Colosseo', true, '15:00:00', '10:00:00', 'Roma', 'IT', 'Perfect for couples who want to plan a romantic escape in Rome.', 2, 210.00, 'Romantic property near Colosseum', '44444444-4444-4444-4444-444444444444'),
('dae58c39-bd49-4511-bf9f-5f0333a6287a', 'Via S. Pasquale', true, '15:00:00', '10:00:00', 'Napoli', 'IT', 'Perfect for families! Enjoy a walk in the promenade.', 4, 85.00, 'Seaside property in Naples', '44444444-4444-4444-4444-444444444444'),
('ad2ba064-1026-4e09-9e40-23c7e36db8be', 'Piazza Municipio', true, '15:00:00', '10:00:00', 'Napoli', 'IT', 'The property is situated near an underground station.', 3, 70.00, 'Perfect property in Naples!', '44444444-4444-4444-4444-444444444444')
ON CONFLICT (property_id) DO NOTHING;

---- Property images
INSERT INTO property_images
(property_image_id, image_url, is_main, property_id)
VALUES
('2d02bc29-0f7a-4ab7-9a4c-46c6ff86e066', 'https://images.pexels.com/photos/749230/pexels-photo-749230.jpeg', false, '8164dd55-a730-4a84-a376-4c6f697aeadb'),
('2191d6ac-d5cf-4c01-ad90-4fd7c55e9df3', 'https://images.pexels.com/photos/2832075/pexels-photo-2832075.jpeg', true, '8164dd55-a730-4a84-a376-4c6f697aeadb'),
('1e75a262-db95-4295-aa56-e6107cfa3f32', 'https://images.pexels.com/photos/3538558/pexels-photo-3538558.jpeg', false, '8164dd55-a730-4a84-a376-4c6f697aeadb'),
('50625ec3-3051-4dd8-a824-770affcc8d4e', 'https://images.pexels.com/photos/2956511/pexels-photo-2956511.jpeg', false, 'fb53eed4-b6c3-467f-b89a-e0babaaa92e0'),
('aced3c31-7663-452d-ad22-b89db51d7501', 'https://images.pexels.com/photos/1268855/pexels-photo-1268855.jpeg', false, 'fb53eed4-b6c3-467f-b89a-e0babaaa92e0'),
('1254e75e-1939-412e-9352-3fe0b1cd67ef', 'https://images.pexels.com/photos/261414/pexels-photo-261414.jpeg', true, 'fb53eed4-b6c3-467f-b89a-e0babaaa92e0'),
('f210018f-c160-4bfc-9b7e-bd08b79a2fb8', 'https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg', false, '191b86ed-c9f9-4713-90e3-167aba489668'),
('414971ca-f234-4988-88be-bae1dda8bc3f', 'https://images.pexels.com/photos/5371575/pexels-photo-5371575.jpeg', true, '191b86ed-c9f9-4713-90e3-167aba489668'),
('5b880643-b96c-4ac4-b7f1-f331cc662e01', 'https://images.pexels.com/photos/6235436/pexels-photo-6235436.jpeg', false, '191b86ed-c9f9-4713-90e3-167aba489668'),
('4821392a-747a-41e2-b064-14378617e398', 'https://images.pexels.com/photos/5997996/pexels-photo-5997996.jpeg', true, 'e9f803e2-89b1-40bc-8c9f-60e0dca8d953'),
('36d6e2d4-ce8b-4389-990c-fa5328ba8eb0', 'https://images.pexels.com/photos/5997992/pexels-photo-5997992.jpeg', false, 'e9f803e2-89b1-40bc-8c9f-60e0dca8d953'),
('95a620d5-d394-4c63-8f8a-384f62c775dc', 'https://images.pexels.com/photos/5563469/pexels-photo-5563469.jpeg', false, 'e9f803e2-89b1-40bc-8c9f-60e0dca8d953'),
('9228b1d5-b43b-4ea4-b56e-a7901ab6f8a5', 'https://images.pexels.com/photos/2956511/pexels-photo-2956511.jpeg', false, '426d603e-3af4-431e-a7c7-ebdd1db1b83a'),
('8aeab4eb-c10c-4dbb-828b-1e10fae5d8c8', 'https://images.pexels.com/photos/261414/pexels-photo-261414.jpeg', true, '426d603e-3af4-431e-a7c7-ebdd1db1b83a'),
('e73b856d-4898-42eb-9be5-3c46bcf79721', 'https://images.pexels.com/photos/1198838/pexels-photo-1198838.jpeg', false, '426d603e-3af4-431e-a7c7-ebdd1db1b83a'),
('95685494-67fb-4778-a0bc-155717c801b1', 'https://images.pexels.com/photos/1268855/pexels-photo-1268855.jpeg', false, '426d603e-3af4-431e-a7c7-ebdd1db1b83a'),
('eb6c3974-f660-45ec-93e9-c4e779e58d74', 'https://images.pexels.com/photos/1457841/pexels-photo-1457841.jpeg', true, 'ad2ba064-1026-4e09-9e40-23c7e36db8be'),
('74969006-1f14-4108-bb57-7652bb624509', 'https://images.pexels.com/photos/591471/pexels-photo-591471.jpeg', false, 'ad2ba064-1026-4e09-9e40-23c7e36db8be'),
('e6365787-6432-4e6d-bd87-61c767184934', 'https://images.pexels.com/photos/1124461/pexels-photo-1124461.jpeg', false, 'ad2ba064-1026-4e09-9e40-23c7e36db8be'),
('b73624d9-e5cb-41b3-a331-d88221989ce9', 'https://images.pexels.com/photos/2826787/pexels-photo-2826787.jpeg', false, 'ad2ba064-1026-4e09-9e40-23c7e36db8be'),
('cac2aaa0-1cd8-499c-bf9e-e0556d1f34d3', 'https://images.pexels.com/photos/2251247/pexels-photo-2251247.jpeg', false, 'ad2ba064-1026-4e09-9e40-23c7e36db8be'),
('d09786f6-fde8-4752-b70c-2aa145ef118a', 'https://images.pexels.com/photos/2183521/pexels-photo-2183521.jpeg', true, 'dae58c39-bd49-4511-bf9f-5f0333a6287a'),
('48d9bbad-2a73-411d-b9de-2eb2be1b6de6', 'https://images.pexels.com/photos/3225527/pexels-photo-3225527.jpeg', false, 'dae58c39-bd49-4511-bf9f-5f0333a6287a'),
('b4562244-3471-417a-8e27-4d1d76cdcff7', 'https://images.pexels.com/photos/459057/pexels-photo-459057.jpeg', false, 'dae58c39-bd49-4511-bf9f-5f0333a6287a'),
('0ec1f2be-b935-4d8c-b12c-21a8ddaec7c9', 'https://images.pexels.com/photos/2527556/pexels-photo-2527556.jpeg', false, 'dae58c39-bd49-4511-bf9f-5f0333a6287a')
ON CONFLICT (property_image_id) DO NOTHING;

---- Property amenities
INSERT INTO public.property_amenity
(property_id, amenity_id)
VALUES
('8164dd55-a730-4a84-a376-4c6f697aeadb', '0ab6b4ad-2fd6-41c0-a4a2-209408a56833'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', '13cbacbf-8198-4406-984f-3baf2a2fb07b'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', '4693d14e-6e4c-4fbe-ba80-2e30f8731ddc'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', '902c993e-6979-4cd5-9ff8-a9f9a7d25781'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', 'f2288bf3-f9e1-40c1-9ffb-8a0086acfb95'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', '43bd47c6-8202-4d4d-aed9-0521be7af9ea'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', '13537009-d406-4b27-80cf-e4099d21048c'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', '57409341-b572-4e57-9215-57ef9f59e6cc'),
('8164dd55-a730-4a84-a376-4c6f697aeadb', 'c21fabcf-2a2a-4e72-a8fd-8256cce59b13'),
('fb53eed4-b6c3-467f-b89a-e0babaaa92e0', '0ab6b4ad-2fd6-41c0-a4a2-209408a56833'),
('fb53eed4-b6c3-467f-b89a-e0babaaa92e0', '902c993e-6979-4cd5-9ff8-a9f9a7d25781'),
('fb53eed4-b6c3-467f-b89a-e0babaaa92e0', '1461547a-592d-4acc-9e11-f06642fad0d4'),
('fb53eed4-b6c3-467f-b89a-e0babaaa92e0', '13cbacbf-8198-4406-984f-3baf2a2fb07b'),
('191b86ed-c9f9-4713-90e3-167aba489668', '4693d14e-6e4c-4fbe-ba80-2e30f8731ddc'),
('191b86ed-c9f9-4713-90e3-167aba489668', 'f2288bf3-f9e1-40c1-9ffb-8a0086acfb95'),
('191b86ed-c9f9-4713-90e3-167aba489668', '43bd47c6-8202-4d4d-aed9-0521be7af9ea'),
('191b86ed-c9f9-4713-90e3-167aba489668', '13537009-d406-4b27-80cf-e4099d21048c'),
('e9f803e2-89b1-40bc-8c9f-60e0dca8d953', '1461547a-592d-4acc-9e11-f06642fad0d4'),
('e9f803e2-89b1-40bc-8c9f-60e0dca8d953', '4693d14e-6e4c-4fbe-ba80-2e30f8731ddc'),
('e9f803e2-89b1-40bc-8c9f-60e0dca8d953', '0ab6b4ad-2fd6-41c0-a4a2-209408a56833'),
('426d603e-3af4-431e-a7c7-ebdd1db1b83a', '4693d14e-6e4c-4fbe-ba80-2e30f8731ddc'),
('426d603e-3af4-431e-a7c7-ebdd1db1b83a', 'cb74f8de-6024-4f06-a62f-fe96fa3990cc'),
('426d603e-3af4-431e-a7c7-ebdd1db1b83a', '43bd47c6-8202-4d4d-aed9-0521be7af9ea'),
('ad2ba064-1026-4e09-9e40-23c7e36db8be', 'c21fabcf-2a2a-4e72-a8fd-8256cce59b13'),
('ad2ba064-1026-4e09-9e40-23c7e36db8be', 'cb74f8de-6024-4f06-a62f-fe96fa3990cc'),
('ad2ba064-1026-4e09-9e40-23c7e36db8be', '13537009-d406-4b27-80cf-e4099d21048c'),
('ad2ba064-1026-4e09-9e40-23c7e36db8be', 'd6f42610-af3b-4409-b6dd-ccde196a20ae'),
('dae58c39-bd49-4511-bf9f-5f0333a6287a', '13537009-d406-4b27-80cf-e4099d21048c'),
('dae58c39-bd49-4511-bf9f-5f0333a6287a', '43bd47c6-8202-4d4d-aed9-0521be7af9ea'),
('dae58c39-bd49-4511-bf9f-5f0333a6287a', 'c21fabcf-2a2a-4e72-a8fd-8256cce59b13'),
('dae58c39-bd49-4511-bf9f-5f0333a6287a', '30b5db79-3225-489d-9df0-bb3902b4392b')
ON CONFLICT (property_id, amenity_id) DO NOTHING;