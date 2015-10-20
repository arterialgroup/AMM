-- BI Pre-disposing Activity

/*-------------------------------------------------------------------*/
-- Module
/*-------------------------------------------------------------------*/
INSERT INTO module(name, description, module_type_id) 
SELECT 
'Reinforcing activity - National Symposium for General Practitioners Optimising Safety of NOACs in Clinical Practice Trials, Tribulations and Truths',
'Complete your reinforcing activity',
id
FROM moduletype 
WHERE name LIKE 'REI%';

/*-------------------------------------------------------------------*/
-- Section
/*-------------------------------------------------------------------*/
INSERT INTO section(name, description, series, module_id, section_type_id)
SELECT
'Reinforcing activity',
'Reinforcing activity',
2,
id,
(SELECT id FROM section_type WHERE name LIKE 'DEF%') AS sec_id
FROM module
WHERE name LIKE 'Reinforcing%'; 

/*-------------------------------------------------------------------*/
-- Step 1
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Reinforcing Question One',
'Reinforcing Question One',
'Reinforcing Question One',
1,
id
FROM section
WHERE name LIKE 'Rei%';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)');

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'- No antithrombotic therapy',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Reinforcing Question One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Less often',
0,
id
FROM question
WHERE text = '- No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 1 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'About the same',
0,
id
FROM question
WHERE text = '- No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 1 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'More often',
0,
id
FROM question
WHERE text = '- No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 2
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'- Aspirin',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Reinforcing Question One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 2 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Less often',
0,
id
FROM question
WHERE text = '- Aspirin';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 2 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'About the same',
0,
id
FROM question
WHERE text = '- Aspirin';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 2 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'More often',
0,
id
FROM question
WHERE text = '- Aspirin';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 3
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'- Vitamin K antagonist (e.g. warfarin)',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Reinforcing Question One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 3 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Less often',
0,
id
FROM question
WHERE text = '- Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 3 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'About the same',
0,
id
FROM question
WHERE text = '- Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 3 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'More often',
0,
id
FROM question
WHERE text = '- Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 4
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'- Dabigatran',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Reinforcing Question One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 4 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Less often',
0,
id
FROM question
WHERE text = '- Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 4 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'About the same',
0,
id
FROM question
WHERE text = '- Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 4 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'More often',
0,
id
FROM question
WHERE text = '- Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'- Rivaroxaban',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Reinforcing Question One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Less often',
0,
id
FROM question
WHERE text = '- Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'About the same',
0,
id
FROM question
WHERE text = '- Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'More often',
0,
id
FROM question
WHERE text = '- Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'- Apixaban',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Based on what you learned during the meeting, please indicate how your recommendation of the following antithrombotic therapies has changed? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Reinforcing Question One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Less often',
0,
id
FROM question
WHERE text = '- Apixaban';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'About the same',
0,
id
FROM question
WHERE text = '- Apixaban';

/*-------------------------------------------------------------------*/
-- Step 1 - Question GROUP - Question 5 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'More often',
0,
id
FROM question
WHERE text = '- Apixaban';

/*-------------------------------------------------------------------*/
-- Step 2
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Reinforcing Question Two',
'Reinforcing Question Two',
'Reinforcing Question Two',
2,
id
FROM section
WHERE name LIKE 'Rei%';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP
/*-------------------------------------------------------------------*/
#INSERT INTO questiongroup(text) VALUES ('Please indicate your level of agreement for the following statements:');

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'I am confident in recommending suitable anticoagulant options across a range of clinical situations.',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Please indicate your level of agreement for the following statements:')
FROM step
WHERE name = 'Reinforcing Question Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Disagree',
0,
id
FROM question
WHERE text = 'I am confident in recommending suitable anticoagulant options across a range of clinical situations.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Agree',
0,
id
FROM question
WHERE text = 'I am confident in recommending suitable anticoagulant options across a range of clinical situations.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Strongly agree',
0,
id
FROM question
WHERE text = 'I am confident in recommending suitable anticoagulant options across a range of clinical situations.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'I am confident in diagnosing non-valvular atrial fibrillation within my practice.',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Please indicate your level of agreement for the following statements:')
FROM step
WHERE name = 'Reinforcing Question Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Disagree',
0,
id
FROM question
WHERE text = 'I am confident in diagnosing non-valvular atrial fibrillation within my practice.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Agree',
0,
id
FROM question
WHERE text = 'I am confident in diagnosing non-valvular atrial fibrillation within my practice.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Strongly agree',
0,
id
FROM question
WHERE text = 'I am confident in diagnosing non-valvular atrial fibrillation within my practice.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'I refer my patients with atrial fibrillation to a specialist for management of stroke prevention.',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Please indicate your level of agreement for the following statements:')
FROM step
WHERE name = 'Reinforcing Question Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Disagree',
0,
id
FROM question
WHERE text = 'I refer my patients with atrial fibrillation to a specialist for management of stroke prevention.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Agree',
0,
id
FROM question
WHERE text = 'I refer my patients with atrial fibrillation to a specialist for management of stroke prevention.';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Strongly agree',
0,
id
FROM question
WHERE text = 'I refer my patients with atrial fibrillation to a specialist for management of stroke prevention.';

/*-------------------------------------------------------------------*/
-- Step 3
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Reinforcing Question Three',
'Reinforcing Question Three',
'Reinforcing Question Three',
4,
id
FROM section
WHERE name LIKE 'Rei%';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('What changes did you implement in your practice as a result of attending this meeting?');

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1 (Free text No Answers)
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'1',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'FREE%'),
id,
(SELECT id FROM questiongroup WHERE text = 'What changes did you implement in your practice as a result of attending this meeting?')
FROM step
WHERE name = 'Reinforcing Question Three';

/*-------------------------------------------------------------------*/
-- Step 4
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Reinforcing Question Four',
'Reinforcing Question Four',
'Reinforcing Question Four',
4,
id
FROM section
WHERE name LIKE 'Rei%';

/*-------------------------------------------------------------------*/
-- Step 4 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('How do you monitor the implementation of these changes?');

/*-------------------------------------------------------------------*/
-- Step 4 - Question GROUP - Question 1 (Free text No Answers)
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'1',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'FREE%'),
id,
(SELECT id FROM questiongroup WHERE text = 'How do you monitor the implementation of these changes?')
FROM step
WHERE name = 'Reinforcing Question Four';

/*-------------------------------------------------------------------*/
-- Step 5
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Reinforcing Question Five',
'Reinforcing Question Five',
'Reinforcing Question Five',
4,
id
FROM section
WHERE name LIKE 'Rei%';

/*-------------------------------------------------------------------*/
-- Step 5 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('What evaluation process do you use to measure the effectiveness of these changes?');

/*-------------------------------------------------------------------*/
-- Step 5 - Question GROUP - Question 1 (Free text No Answers)
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'1',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'FREE%'),
id,
(SELECT id FROM questiongroup WHERE text = 'What evaluation process do you use to measure the effectiveness of these changes?')
FROM step
WHERE name = 'Reinforcing Question Five';