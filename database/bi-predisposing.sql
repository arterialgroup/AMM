-- BI Pre-disposing Activity
DELETE FROM answer;
DELETE FROM question;
DELETE FROM questiongroup;
DELETE FROM step;
DELETE FROM section;
DELETE FROM module;

/*-------------------------------------------------------------------*/
-- Module
/*-------------------------------------------------------------------*/
INSERT INTO module(name, description, module_type_id) 
SELECT 
'National Symposium Pre-disposing',
'National Symposium Pre-disposing Activity',
id
FROM moduletype 
WHERE name LIKE 'PRE%';

/*-------------------------------------------------------------------*/
-- Section
/*-------------------------------------------------------------------*/
INSERT INTO section(name, description, series, module_id, section_type_id)
SELECT
'Pre-disposing Section',
'Pre-disposing Section',
1,
id,
(SELECT id FROM section_type WHERE name LIKE 'DEF%') AS sec_id
FROM module
WHERE name LIKE 'National%'; 

/*-------------------------------------------------------------------*/
-- Step 1
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step One',
'Step One',
'Step One',
1,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 1 - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'How many patients with atrial fibrillation do you manage in your practice?',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
null
FROM step
WHERE name = 'Step One';

/*-------------------------------------------------------------------*/
-- Step 1 - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-2',
0,
id
FROM question
WHERE text LIKE 'How many patients with atrial fibrillation%';

/*-------------------------------------------------------------------*/
-- Step 1 - Question 1- Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'3-5',
0,
id
FROM question
WHERE text LIKE 'How many patients with atrial fibrillation%';

/*-------------------------------------------------------------------*/
-- Step 1 - Question 1- Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'5-10',
0,
id
FROM question
WHERE text LIKE 'How many patients with atrial fibrillation%';

/*-------------------------------------------------------------------*/
-- Step 1 - Question 1- Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'>10',
0,
id
FROM question
WHERE text LIKE 'How many patients with atrial fibrillation%';

/*-------------------------------------------------------------------*/
-- Step 2
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step Two',
'Step Two',
'Step Two',
2,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('Is the following information recorded for your patients with atrial fibrillation?');

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'CHADS2',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Is the following information recorded for your patients with atrial fibrillation?')
FROM step
WHERE name = 'Step Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Never',
0,
id
FROM question
WHERE text = 'CHADS2';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Sometimes',
0,
id
FROM question
WHERE text = 'CHADS2';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 1 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Always',
0,
id
FROM question
WHERE text = 'CHADS2';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'CHA2DS2VASc',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Is the following information recorded for your patients with atrial fibrillation?')
FROM step
WHERE name = 'Step Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Never',
0,
id
FROM question
WHERE text = 'CHA2DS2VASc';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Sometimes',
0,
id
FROM question
WHERE text = 'CHA2DS2VASc';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 2 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Always',
0,
id
FROM question
WHERE text = 'CHA2DS2VASc';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'HAS-BLED',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Is the following information recorded for your patients with atrial fibrillation?')
FROM step
WHERE name = 'Step Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Never',
0,
id
FROM question
WHERE text = 'HAS-BLED';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Sometimes',
0,
id
FROM question
WHERE text = 'HAS-BLED';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 3 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Always',
0,
id
FROM question
WHERE text = 'HAS-BLED';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 4
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Creatine Clearance',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Is the following information recorded for your patients with atrial fibrillation?')
FROM step
WHERE name = 'Step Two';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 4 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Never',
0,
id
FROM question
WHERE text = 'Creatine Clearance';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 4 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Sometimes',
0,
id
FROM question
WHERE text = 'Creatine Clearance';

/*-------------------------------------------------------------------*/
-- Step 2 - Question GROUP - Question 4 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'Always',
0,
id
FROM question
WHERE text = 'Creatine Clearance';

/*-------------------------------------------------------------------*/
-- Step 3
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step Three',
'Step Three',
'Step Three',
3,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)');

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'No antithrombotic therapy',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 1 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'No antithrombotic therapy';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Aspirin',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'Aspirin';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'Aspirin';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'Aspirin';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'Aspirin';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'Aspirin';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 3
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Vitamin K antagonist (e.g. warfarin)',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 3 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 3 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 3 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 3 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 2 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'Vitamin K antagonist (e.g. warfarin)';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 4
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Dabigatran',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 4 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 4 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 4 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 4 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 4 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'Dabigatran';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Rivaroxaban',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'Rivaroxaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Apixaban',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'Apixaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'Apixaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'Apixaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'Apixaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'Apixaban';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Other',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'For what proportion of your patients do you recommend each of the following antithrombotic therapies? (tick one box for each therapeutic option)')
FROM step
WHERE name = 'Step Three';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'0-20%',
0,
id
FROM question
WHERE text = 'Other';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'21-40%',
0,
id
FROM question
WHERE text = 'Other';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'41-60%',
0,
id
FROM question
WHERE text = 'Other';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'61-80%',
0,
id
FROM question
WHERE text = 'Other';

/*-------------------------------------------------------------------*/
-- Step 3 - Question GROUP - Question 5 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'81-100%',
0,
id
FROM question
WHERE text = 'Other';

/*-------------------------------------------------------------------*/
-- Step 4
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step Four',
'Step Four',
'Step Four',
4,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 4 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('How do you counsel patients when commencing them on anticoagulant therapy? (list two pieces of advice that you always include in a consultation before initiating therapy)');

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
(SELECT id FROM questiongroup WHERE text = 'How do you counsel patients when commencing them on anticoagulant therapy? (list two pieces of advice that you always include in a consultation before initiating therapy)')
FROM step
WHERE name = 'Step Four';

/*-------------------------------------------------------------------*/
-- Step 4 - Question GROUP - Question 2 (Free text No Answers)
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'2',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'FREE%'),
id,
(SELECT id FROM questiongroup WHERE text = 'How do you counsel patients when commencing them on anticoagulant therapy? (list two pieces of advice that you always include in a consultation before initiating therapy)')
FROM step
WHERE name = 'Step Four';

/*-------------------------------------------------------------------*/
-- Step 5
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step Five',
'Step Five',
'Step Five',
5,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 5 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('How do you minimise the risk of bleeding in your patients on antithrombotic therapy? (list two management options that you currently recommend)');

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
(SELECT id FROM questiongroup WHERE text = 'How do you minimise the risk of bleeding in your patients on antithrombotic therapy? (list two management options that you currently recommend)')
FROM step
WHERE name = 'Step Five';

/*-------------------------------------------------------------------*/
-- Step 5 - Question GROUP - Question 2 (Free text No Answers)
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'2',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'FREE%'),
id,
(SELECT id FROM questiongroup WHERE text = 'How do you minimise the risk of bleeding in your patients on antithrombotic therapy? (list two management options that you currently recommend)')
FROM step
WHERE name = 'Step Five';

/*-------------------------------------------------------------------*/
-- Step 6
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step Six',
'Step Six',
'Step Six',
6,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 6 - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'A 65 year old patient with normal renal function who is currently taking a NOAC requires a skin cancer excision. How long before the procedure would you recommend the patient takes their last NOAC dose?',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
null
FROM step
WHERE name = 'Step Six';

/*-------------------------------------------------------------------*/
-- Step 6 - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'>72 hours',
0,
id
FROM question
WHERE text LIKE 'A 65 year old patient%';

/*-------------------------------------------------------------------*/
-- Step 6 - Question 1 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'72 hours',
0,
id
FROM question
WHERE text LIKE 'A 65 year old patient%';

/*-------------------------------------------------------------------*/
-- Step 6 - Question 1 - Answer 3
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'48 hours',
0,
id
FROM question
WHERE text LIKE 'A 65 year old patient%';

/*-------------------------------------------------------------------*/
-- Step 6 - Question 1 - Answer 4
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'24 hours',
0,
id
FROM question
WHERE text LIKE 'A 65 year old patient%';

/*-------------------------------------------------------------------*/
-- Step 6 - Question 1 - Answer 5
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'no does interruption required',
0,
id
FROM question
WHERE text LIKE 'A 65 year old patient%';

/*-------------------------------------------------------------------*/
-- Step 7
/*-------------------------------------------------------------------*/
INSERT INTO step(name, description, content, series, section_id) 
SELECT
'Step Seven',
'Step Seven',
'Step Seven',
7,
id
FROM section
WHERE name LIKE 'Pre%';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP
/*-------------------------------------------------------------------*/
INSERT INTO questiongroup(text) VALUES ('Please provide your opinion on the following:');

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 1
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Warfarin is the first-line option for stroke prevention in all patients with NVAF',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Please provide your opinion on the following:')
FROM step
WHERE name = 'Step Seven';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 1 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'True',
0,
id
FROM question
WHERE text = 'Warfarin is the first-line option for stroke prevention in all patients with NVAF';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 1 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'False',
0,
id
FROM question
WHERE text = 'Warfarin is the first-line option for stroke prevention in all patients with NVAF';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 2
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'NOACs can be used in elderly patients with NVAF',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Please provide your opinion on the following:')
FROM step
WHERE name = 'Step Seven';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 2 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'True',
0,
id
FROM question
WHERE text = 'NOACs can be used in elderly patients with NVAF';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 2 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'False',
0,
id
FROM question
WHERE text = 'NOACs can be used in elderly patients with NVAF';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 3
/*-------------------------------------------------------------------*/
INSERT INTO question(text, series, showResults, question_type_id, step_id, question_group_id) 
SELECT
'Managing surgical procedures is more problematic with NOACs than with warfarin',
1,
0,
(SELECT id FROM question_type WHERE name LIKE 'RAD%'),
id,
(SELECT id FROM questiongroup WHERE text = 'Please provide your opinion on the following:')
FROM step
WHERE name = 'Step Seven';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 3 - Answer 1
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'True',
0,
id
FROM question
WHERE text = 'Managing surgical procedures is more problematic with NOACs than with warfarin';

/*-------------------------------------------------------------------*/
-- Step 7 - Question GROUP - Question 3 - Answer 2
/*-------------------------------------------------------------------*/
INSERT INTO answer(text, correct, question_id) 
SELECT
'False',
0,
id
FROM question
WHERE text = 'Managing surgical procedures is more problematic with NOACs than with warfarin';






