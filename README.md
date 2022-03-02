# mBERT
**mBERT** is a mutation testing tool that uses a pre-trained language model (*CodeBERT*) to generate mutants.  


## Requirements
- Maven
- *CodeBERT* dependencies:  
	- 'pip install torch'
	- 'pip install transformers'

## Installation
1. Run 'mkdir -p pre-trained/codebert-base-mlm' to create the folder where *CodeBERT* pre-trained model will be saved.
2. Run 'python3 download-codebert.py' to download *CodeBERT* pre-trained model.
3. Try *CodeBERT* by running: 'python3 run-codebert.py \"int \<mask\> = b;\"'
> {'score': 0.23396340012550354, 'token': 740, 'token_str': 'c', 'sequence': 'int c= b;'}
> 
> {'score': 0.05450829118490219, 'token': 939, 'token_str': 'i', 'sequence': 'int i= b;'}
> 
> {'score': 0.05004948750138283, 'token': 741, 'token_str': 'b', 'sequence': 'int b= b;'}
> 
> {'score': 0.04164685681462288, 'token': 10, 'token_str': 'a', 'sequence': 'int a= b;'}
> 
> {'score': 0.023635799065232277, 'token': 181, 'token_str': 'p', 'sequence': 'int p= b;'}\

4. Compile by running 'mvn compile'. 
5. You are done! Try mBERT by running: './mBERT.sh'

## Executing mBERT
mBERT provides some flags that you can configure:
- '-in=source_file_name' 
- '-out=mutants_directory'
- '-N=max_num_of_mutants'
- '-m=method_name'
- '-m=method_name:method_definition_line'
- '-l=line_to_mutate'

## Compile Mutants Generated

You can use script 'compile-mutants.sh' to compile the mutants generated by **mBERT**.

- Usage: './compile-mutants.sh mutants_dir subject_name' 
- Information: After compiling the mutants, you will find the details in files 'mutants_dir/subject_name.csv' and 'mutants_dir/subject_name.log' 
- Example: './compile-mutants.sh examples/generated-mutants/gcd/ gcd'

## Examples
Forlder 'examples' provide the examples discussed in the paper. Inside 'examples/generated-mutants' you can find the mutants generated by **mBERT**.

