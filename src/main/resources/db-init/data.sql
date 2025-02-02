
INSERT INTO country (name, region)
VALUES
    ('Japon', 'ASIA'),
    ('France', 'EUROPE'),
    ('Nigeria', 'AFRICA'),
    ('China', 'ASIA'),
    ('Brazil', 'SOUTH_AMERICA'),
    ('United States', 'NORTH_AMERICA'),
    ('Australia', 'OCEANIA'),
    ('Argentina', 'SOUTH_AMERICA');

INSERT INTO locations (location_name, country_id, region, latitude, longitude, location_type)
VALUES
    ('Tokyo Tower', 1, 'Kanto', 35.6586, 139.7454, 'MONUMENT'),
    ('Kyoto Imperial Palace', 1, 'Kansai', 35.0274, 135.7841, 'HISTORICAL_SITE'),
    ('Mount Fuji', 1, 'Chubu', 35.3606, 138.7274, 'MONUMENT'),
    ('Nara Park', 1, 'Kinki', 34.6851, 135.8050, 'PARK'),
    ('Fushimi Inari Taisha', 1, 'Kansai', 34.9667, 135.7722, 'RELIGIOUS_MONUMENT'),
    ('Osaka Castle', 1, 'Kansai', 34.6873, 135.5262, 'CASTLE'),
    ('Shibuya Crossing', 1, 'Kanto', 35.6595, 139.7004, 'CITY'),
    ('Hiroshima Peace Memorial Park', 1, 'Chugoku', 34.3955, 132.4537, 'HISTORICAL_SITE'),
    ('Ueno Zoo', 1, 'Kanto', 35.7138, 139.7740, 'ZOO'),
    ('Roppongi Hills', 1, 'Kanto', 35.6605, 139.7316, 'SHOPPING_MALL'),
    ('Meiji Shrine', 1, 'Kanto', 35.6764, 139.6993, 'TEMPLE'),
    ('Odaiba', 1, 'Kanto', 35.6193, 139.7760, 'AMUSEMENT_PARK'),
    ('Ginza', 1, 'Kanto', 35.6719, 139.7649, 'SHOPPING_MALL'),
    ('Kinkaku-ji (Golden Pavilion)', 1, 'Kansai', 35.0394, 135.7292, 'MONUMENT'),
    ('Senso-ji Temple', 1, 'Kanto', 35.7148, 139.7967, 'TEMPLE');

INSERT INTO photos (url, uploaded_at, user_id)
VALUES
    ('https://example.com/photo1.jpg', '2025-01-22', 1),
    ('https://example.com/photo2.jpg', '2025-01-23', 2),
    ('https://example.com/photo3.jpg', '2025-01-24', 3),
    ('https://example.com/photo4.jpg', '2025-03-25', 1),
    ('https://example.com/photo5.jpg', '2025-03-02', 2);

INSERT INTO diary (name_diary, user_id, photo_cover_id, country_id, date_create_diary, date_start_diary, date_end_diary, description_diary, public_diary)
VALUES
    ('Voyage au Japon', 1, 1, 1, '2025-01-15', '2025-02-01', '2025-02-28', 'Un voyage inoubliable à travers Tokyo, Kyoto et Osaka, à la découverte de la culture japonaise.', true),
    ('Séjour à Hokkaido', 2, 2, 1, '2025-01-20', '2025-03-01', '2025-03-10', 'Découverte des paysages enneigés et des sources chaudes d’Hokkaido, un véritable havre de paix.', false),
    ('Tokyo et ses alentours', 3, 3, 1, '2025-01-25', '2025-04-01', '2025-04-15', 'Séjour à Tokyo avec des excursions dans la baie de Tokyo, Akihabara, et les temples alentours.', true),
    ('Les temples de Kyoto', 4, 4, 1, '2025-02-01', '2025-05-01', '2025-05-15', 'Une immersion dans l’histoire du Japon à travers les temples majestueux de Kyoto, classés au patrimoine mondial.', true),
    ('Road trip au Japon', 1, 5, 1, '2025-02-10', '2025-06-01', '2025-06-30', 'Voyage en voiture à travers le Japon pour explorer ses régions rurales et ses trésors cachés.', false);

INSERT INTO diary_entry (title, note, date_created, description, user_id, diary_id, location_id)
VALUES
    ('Visite du Temple Kinkaku-ji', 'Magnifique temple en or à Kyoto.', '2025-02-01', 'Une expérience inoubliable de paix et de sérénité.', 1, 1, 1),
    ('Dîner à Tsukiji', 'Sushi incroyable au marché de Tsukiji.', '2025-01-23', 'Le meilleur sushi de ma vie !', 2, 2, 2),
    ('Promenade à Shibuya', 'Vue spectaculaire de l’intersection de Shibuya.', '2025-01-24', 'Un endroit très animé et dynamique, idéal pour les jeunes.', 3, 3, 3),
    ('Pique-nique sous les cerisiers', 'Sous les cerisiers en fleurs à Ueno.', '2025-03-25', 'L’atmosphère était magique et le temps magnifique.', 1, 1, 4),
    ('Le marché de Nishiki', 'Découverte des spécialités locales.', '2025-03-02', 'Des souvenirs incroyables de Kyoto et de sa cuisine.', 1, 5, 5);

INSERT INTO vocabulary (expression, translation, usage_example, language_expression, language_translation)
VALUES
    ('Sakura', 'Cherry blossom', 'La saison des sakura au Japon est très belle.', 'JAPANESE', 'FRENCH'),
    ('Konnichiwa', 'Hello', 'Konnichiwa ! Comment ça va ?', 'JAPANESE', 'FRENCH'),
    ('Arigatou', 'Thank you', 'Arigatou gozaimasu pour le repas.', 'JAPANESE', 'FRENCH'),
    ('Sumimasen', 'Excuse me', 'Sumimasen, où est la gare ?', 'JAPANESE', 'FRENCH'),
    ('Bento', 'Lunch box', 'Un bento est un déjeuner japonais traditionnel.', 'JAPANESE', 'FRENCH');

INSERT INTO photos (url, uploaded_at, user_id)
VALUES
    ('https://example.com/photo1.jpg', '2025-01-22', 1),
    ('https://example.com/photo2.jpg', '2025-01-23', 2),
    ('https://example.com/photo3.jpg', '2025-01-24', 3),
    ('https://example.com/photo4.jpg', '2025-03-25', 4),
    ('https://example.com/photo5.jpg', '2025-03-02', 1);

INSERT INTO diary_entry_vocabulary (diary_entry_id, vocabulary_id)
VALUES
    (1, 1), (1, 2),  -- "Visite du Temple Kinkaku-ji" avec "Sakura" et "Konnichiwa"
    (2, 3), (2, 4),  -- "Dîner à Tsukiji" avec "Arigatou" et "Sumimasen"
    (3, 5), (3, 1),  -- "Promenade à Shibuya" avec "Bento" et "Sakura"
    (4, 2), (4, 3),  -- "Pique-nique sous les cerisiers" avec "Konnichiwa" et "Arigatou"
    (5, 4), (5, 5);  -- "Le marché de Nishiki" avec "Sumimasen" et "Bento"



