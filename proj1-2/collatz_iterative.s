# CS61C Sp14 Project 1-2
# Task A: The Collatz conjecture

.globl main
.include "collatz_common.s"

main:
	jal read_input			# Get the number we wish to try the Collatz conjecture on
	move $a0, $v0			# Set $a0 to the value read
	la $a1, collatz_iterative	# Set $a1 as ptr to the function we want to execute
	jal execute_function		# Execute the function
	li $v0, 10			# Exit
	syscall
	
# --------------------- DO NOT MODIFY ANYTHING ABOVE THIS POINT ---------------------

# Returns the stopping time of the Collatz function (the number of steps taken till the number reaches one)
# using an ITERATIVE approach. This means that if the input is 1, your function should return 0.
#
# The initial value is stored in $a0, and you may assume that it is a positive number.
# 
# Make sure to follow all function call conventions.
collatz_iterative:
	j helper
helper:
	bne $a0 1 loop
	add $v0 $t1 $0
	jr $ra
	
	
	
loop:
	add $t0 $a0 $0
	andi $t0 $t0 1
	beq $t0 0 even
	j odd

even:
	sra $a0 $a0 1
	addi $t1 $t1 1
	j helper

odd:
	addi $t0 $0 3
	mul $a0 $a0 $t0
	addi $a0 $a0 1
	addi $t1 $t1 1
	j helper
	

