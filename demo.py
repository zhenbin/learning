from numpy import ones, dot, array, mat, multiply

arr1 = ones((3, 4))
arr2 = ones((4, 3)) * 2
arr3 = ones((4, 3)) * 2
print arr1

print arr3

print arr1.shape, arr1.shape[0]

print "dot:", dot(arr1, arr2)

print "*", arr2 * arr3

arr2[:, 0] = 0
print arr2
arr2[0, :] = 0
print arr2
arr2[1:3, 2:] = 0
print arr2
print arr2[:, 1].min(), arr2[:, 1].max()

a = mat([-1, 2, 3])
b = mat([-1, -1, -1])
print mat([-1, 2, 3]).T[:, 0].min()
print multiply(a.T, b.T)

c = mat([[1, 1], [2, 2]])
d = array([[1, 1], [2, 2]])
print array(c)[0]
