def check_conditions(a, b, c, d, e, f, g, h, i, j):
    return not ((b and not a) or (c and not b) or (d and not a) or (e and not d) or (f and not (b and d)) or (g and not (e and f)) or (h and not d) or (i and not (e and h)) or (j and not i))

num_of_ideals = 0

for a in range(2):
    for b in range(2):
        for c in range(2):
            for d in range(2):
                for e in range(2):
                    for f in range(2):
                        for g in range(2):
                            for h in range(2):
                                for i in range(2):
                                    for j in range(2):
                                        if check_conditions(a, b, c, d, e, f, g, h, i, j):
                                            num_of_ideals += 1

print(num_of_ideals) # gives 42 :o