package com.prepwise.config;

import com.prepwise.entity.CodingCompany;
import com.prepwise.entity.CodingDifficulty;
import com.prepwise.entity.CodingProblem;
import com.prepwise.entity.CodingTestCase;
import com.prepwise.entity.CodingTopic;
import com.prepwise.repository.CodingProblemRepository;
import com.prepwise.repository.CodingTestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingProblemSeeder implements CommandLineRunner {

    private final CodingProblemRepository problemRepository;
    private final CodingTestCaseRepository testCaseRepository;


    @Override
    public void run(String... args) {
        if (!problemRepository.existsBySlug("two-sum")) {
            seedTwoSum();
        }

        if (!problemRepository.existsBySlug("valid-parentheses")) {
            seedValidParentheses();
        }

        if (!problemRepository.existsBySlug("reverse-linked-list")) {
            seedReverseArray();
        }

        if (!problemRepository.existsBySlug("maximum-subarray")) {
            seedMaximumSubarray();
        }

        if (!problemRepository.existsBySlug("nth-fibonacci-number")) {
            seedFibonacci();
        }
        if (!problemRepository.existsBySlug("palindrome-number")) {
            seedPalindromeNumber();
        }

        if (!problemRepository.existsBySlug("count-vowels")) {
            seedCountVowels();
        }

        if (!problemRepository.existsBySlug("largest-array-element")) {
            seedLargestArrayElement();
        }

        if (!problemRepository.existsBySlug("check-prime-number")) {
            seedCheckPrimeNumber();
        }
        if (!problemRepository.existsBySlug("best-time-stock")) {
            seedBestTimeStock();
        }

        if (!problemRepository.existsBySlug("first-non-repeating-character")) {
            seedFirstNonRepeatingCharacter();
        }

        if (!problemRepository.existsBySlug("sum-of-digits")) {
            seedSumOfDigits();
        }

        if (!problemRepository.existsBySlug("binary-tree-level-order")) {
            seedBinaryTreeLevelOrder();
        }

        if (!problemRepository.existsBySlug("move-zeros-to-end")) {
            seedMoveZerosToEnd();
        }

        if (!problemRepository.existsBySlug("second-largest-element")) {
            seedSecondLargestElement();
        }

        if (!problemRepository.existsBySlug("anagram-check")) {
            seedAnagramCheck();
        }

        if (!problemRepository.existsBySlug("string-reversal")) {
            seedStringReversal();
        }

        if (!problemRepository.existsBySlug("missing-number")) {
            seedMissingNumber();
        }

        if (!problemRepository.existsBySlug("remove-duplicates-array")) {
            seedRemoveDuplicatesArray();
        }
        if (!problemRepository.existsBySlug("reverse-a-number")) {
            seedReverseANumber();
        }

        if (!problemRepository.existsBySlug("gcd-of-two-numbers")) {
            seedGCDOfTwoNumbers();
        }

        if (!problemRepository.existsBySlug("armstrong-number")) {
            seedArmstrongNumber();
        }

        if (!problemRepository.existsBySlug("longest-substring-without-repeating-characters")) {
            seedLongestSubstringWithoutRepeatingCharacters();
        }

        if (!problemRepository.existsBySlug("product-of-array-except-self")) {
            seedProductOfArrayExceptSelf();
        }

        if (!problemRepository.existsBySlug("second-most-frequent-character")) {
            seedSecondMostFrequentCharacter();
        }

        if (!problemRepository.existsBySlug("sort-012-array")) {
            seedSort012Array();
        }

        if (!problemRepository.existsBySlug("remove-spaces-from-string")) {
            seedRemoveSpacesFromString();
        }

        if (!problemRepository.existsBySlug("anagram-palindrome-check")) {
            seedAnagramPalindromeCheck();
        }

        if (!problemRepository.existsBySlug("find-missing-character")) {
            seedFindMissingCharacter();
        }

        if (!problemRepository.existsBySlug("count-words-in-string")) {
            seedCountWordsInString();
        }

        if (!problemRepository.existsBySlug("binary-search")) {
            seedBinarySearch();
        }

        if (!problemRepository.existsBySlug("merge-intervals")) {
            seedMergeIntervals();
        }

        if (!problemRepository.existsBySlug("longest-palindromic-substring")) {
            seedLongestPalindromicSubstring();
        }

        if (!problemRepository.existsBySlug("rotate-array-by-k")) {
            seedRotateArrayByK();
        }

        if (!problemRepository.existsBySlug("find-duplicate-number")) {
            seedFindDuplicateNumber();
        }

        if (!problemRepository.existsBySlug("matrix-diagonal-sum")) {
            seedMatrixDiagonalSum();
        }

        if (!problemRepository.existsBySlug("climbing-stairs")) {
            seedClimbingStairs();
        }

        if (!problemRepository.existsBySlug("count-set-bits")) {
            seedCountSetBits();
        }

        if (!problemRepository.existsBySlug("coin-change")) {
            seedCoinChange();
        }

        if (!problemRepository.existsBySlug("validate-bst")) {
            seedValidateBST();
        }

        if (!problemRepository.existsBySlug("lowest-common-ancestor-bst")) {
            seedLowestCommonAncestorBST();
        }

        if (!problemRepository.existsBySlug("merge-two-sorted-linked-lists")) {
            seedMergeTwoSortedLinkedLists();
        }
        if (!problemRepository.existsBySlug("equilibrium-index")) {
            seedEquilibriumIndex();
        }
        if (!problemRepository.existsBySlug("leaders-in-array")) {
            seedLeadersInArray();
        }
        if (!problemRepository.existsBySlug("kadanes-algorithm")) {
            seedKadanesAlgorithm();
        }





        // Additional company-wise questions will be seeded here.
    }
    private void seedKadanesAlgorithm() {
        CodingProblem problem = CodingProblem.builder()
                .title("Kadane's Algorithm")
                .slug("kadanes-algorithm")
                .description("Given an integer array, find the maximum sum of any non-empty contiguous subarray.")
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.TCS)
                .starterCodeJava("""
                import java.util.*;
                class Solution {
                    public static int maxSubarraySum(int[] arr) {
                        // Write your code here
                        return 0;
                    }
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        int n = sc.nextInt();
                        int[] arr = new int[n];
                        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();
                        System.out.println(maxSubarraySum(arr));
                    }
                }
                """)
                .starterCodePython("""
                def max_subarray_sum(arr):
                    # Write your code here
                    return 0

                n = int(input())
                arr = list(map(int, input().split()))
                print(max_subarray_sum(arr))
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int maxSubarraySum(vector<int>& arr) {
                    // Write your code here
                    return 0;
                }
                int main() {
                    int n; cin >> n;
                    vector<int> arr(n);
                    for (int &x : arr) cin >> x;
                    cout << maxSubarraySum(arr) << endl;
                    return 0;
                }
                """)
                .starterCodeJavascript("""
                function maxSubarraySum(arr) {
                    // Write your code here
                    return 0;
                }
                const fs = require('fs');
                const input = fs.readFileSync(0, 'utf8').trim().split(/\\\\s+/).map(Number);
                const n = input[0];
                console.log(maxSubarraySum(input.slice(1, n + 1)));
                """)
                .build();

        problemRepository.save(problem);
        saveTestCases(problem,
                new String[]{"9\n-2 1 -3 4 -1 2 1 -5 4", "6", "true"},
                new String[]{"5\n1 2 3 4 5", "15", "true"},
                new String[]{"4\n-8 -3 -6 -2 -5", "-2", "false"},
                new String[]{"1\n7", "7", "false"}
        );
    }
    private void seedLeadersInArray() {
        CodingProblem problem = CodingProblem.builder()
                .title("Leaders in an Array")
                .slug("leaders-in-array")
                .description("Given an integer array, find all leaders. An element is a leader if it is greater than or equal to every element to its right. Print the leaders in their original left-to-right order, separated by spaces.")
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.TCS)
                .starterCodeJava("""
                import java.util.*;
                class Solution {
                    public static List<Integer> leaders(int[] arr) {
                        // Write your code here
                        return new ArrayList<>();
                    }
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        int n = sc.nextInt();
                        int[] arr = new int[n];
                        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();
                        List<Integer> result = leaders(arr);
                        for (int i = 0; i < result.size(); i++) {
                            if (i > 0) System.out.print(" ");
                            System.out.print(result.get(i));
                        }
                        System.out.println();
                    }
                }
                """)
                .starterCodePython("""
                def leaders(arr):
                    # Write your code here
                    return []

                n = int(input())
                arr = list(map(int, input().split()))
                print(*leaders(arr))
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                vector<int> leaders(vector<int>& arr) {
                    // Write your code here
                    return {};
                }
                int main() {
                    int n; cin >> n;
                    vector<int> arr(n);
                    for (int &x : arr) cin >> x;
                    vector<int> result = leaders(arr);
                    for (int i = 0; i < result.size(); i++) {
                        if (i) cout << " ";
                        cout << result[i];
                    }
                    cout << endl;
                    return 0;
                }
                """)
                .starterCodeJavascript("""
                function leaders(arr) {
                    // Write your code here
                    return [];
                }
                const fs = require('fs');
                const input = fs.readFileSync(0, 'utf8').trim().split(/\\\\s+/).map(Number);
                const n = input[0];
                console.log(leaders(input.slice(1, n + 1)).join(' '));
                """)
                .build();

        problemRepository.save(problem);
        saveTestCases(problem,
                new String[]{"6\\n16 17 4 3 5 2", "17 5 2", "true"},
                new String[]{"5\\n1 2 3 4 5", "5", "true"},
                new String[]{"4\\n7 7 3 1", "7 7 3 1", "false"},
                new String[]{"1\\n9", "9", "false"}
        );
    }
    private void seedEquilibriumIndex() {
        CodingProblem problem = CodingProblem.builder()
                .title("Equilibrium Index")
                .slug("equilibrium-index")
                .description("Given an integer array, return the 0-based index where the sum of elements to its left equals the sum of elements to its right. Return -1 if no such index exists.")
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.TCS)
                .starterCodeJava("""
                import java.util.*;
                class Solution {
                    public static int equilibriumIndex(int[] arr) {
                        // Write your code here
                        return -1;
                    }
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        int n = sc.nextInt();
                        int[] arr = new int[n];
                        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();
                        System.out.println(equilibriumIndex(arr));
                    }
                }
                """)
                .starterCodePython("""
                def equilibrium_index(arr):
                    # Write your code here
                    return -1

                n = int(input())
                arr = list(map(int, input().split()))
                print(equilibrium_index(arr))
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int equilibriumIndex(vector<int>& arr) {
                    // Write your code here
                    return -1;
                }
                int main() {
                    int n; cin >> n;
                    vector<int> arr(n);
                    for (int &x : arr) cin >> x;
                    cout << equilibriumIndex(arr) << endl;
                    return 0;
                }
                """)
                .starterCodeJavascript("""
                function equilibriumIndex(arr) {
                    // Write your code here
                    return -1;
                }
                const fs = require('fs');
                const input = fs.readFileSync(0, 'utf8').trim().split(/\\\\s+/).map(Number);
                const n = input[0];
                console.log(equilibriumIndex(input.slice(1, n + 1)));
                """)
                .build();

        problemRepository.save(problem);
        saveTestCases(problem,
                new String[]{"5\\n1 3 5 2 2", "2", "true"},
                new String[]{"3\\n1 2 3", "-1", "true"},
                new String[]{"1\\n10", "0", "false"},
                new String[]{"5\\n2 1  -1 1 2", "0", "false"}
        );
    }

    private void seedValidateBST() {
        CodingProblem problem = CodingProblem.builder()
                .title("Validate Binary Search Tree")
                .slug("validate-bst")
                .description("""
                    Given n level-order values of a binary tree, where -1 represents
                    a missing node, determine whether the tree is a valid BST.
                    Assume node values are distinct. Print true or false.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.TREES)
                .company(CodingCompany.MICROSOFT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        static class Node {
                            int val; Node left, right;
                            Node(int v) { val = v; }
                        }
                        static boolean valid(Node node, long low, long high) {
                            if (node == null) return true;
                            if (node.val <= low || node.val >= high) return false;
                            return valid(node.left, low, node.val)
                                && valid(node.right, node.val, high);
                        }
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            if (n == 0) {
                                System.out.println("true");
                                return;
                            }
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            if (a[0] == -1) {
                                System.out.println("true");
                                return;
                            }
                            Node[] nodes = new Node[n];
                            for (int i = 0; i < n; i++)
                                if (a[i] != -1) nodes[i] = new Node(a[i]);
                            for (int i = 0; i < n; i++) {
                                if (nodes[i] != null) {
                                    int l = 2 * i + 1, r = 2 * i + 2;
                                    if (l < n) nodes[i].left = nodes[l];
                                    if (r < n) nodes[i].right = nodes[r];
                                }
                            }
                            System.out.println(valid(nodes[0], Long.MIN_VALUE, Long.MAX_VALUE)
                                ? "true" : "false");
                        }
                    }
                    """)
                .starterCodePython("""
                    import sys
                    a = list(map(int, sys.stdin.read().split()))
                    n = a[0]
                    vals = a[1:1+n]
                    if n == 0 or vals[0] == -1:
                        print("true")
                    else:
                        def valid(i, low, high):
                            if i >= n or vals[i] == -1:
                                return True
                            x = vals[i]
                            if not low < x < high:
                                return False
                            return valid(2*i+1, low, x) and valid(2*i+2, x, high)
                        print("true" if valid(0, float('-inf'), float('inf')) else "false")
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    #include <climits>
                    using namespace std;
                    bool valid(vector<int>& a, int i, long long low, long long high) {
                        if (i >= (int)a.size() || a[i] == -1) return true;
                        long long x = a[i];
                        if (x <= low || x >= high) return false;
                        return valid(a, 2*i+1, low, x) && valid(a, 2*i+2, x, high);
                    }
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        cout << ((n == 0 || a[0] == -1 ||
                            valid(a, 0, LLONG_MIN, LLONG_MAX)) ? "true" : "false");
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], a = v.slice(1, n + 1);
                    function valid(i, low, high) {
                        if (i >= n || a[i] === -1) return true;
                        const x = a[i];
                        if (x <= low || x >= high) return false;
                        return valid(2*i+1, low, x) && valid(2*i+2, x, high);
                    }
                    console.log(n === 0 || a[0] === -1 ||
                        valid(0, -Infinity, Infinity) ? 'true' : 'false');
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"7\n8 4 12 2 6 10 14", "true", "true"},
                new String[]{"3\n5 7 4", "false", "false"},
                new String[]{"3\n2 1 3", "true", "false"},
                new String[]{"3\n10 5 15", "true", "false"}
        );
    }

    private void seedLowestCommonAncestorBST() {
        CodingProblem problem = CodingProblem.builder()
                .title("Lowest Common Ancestor in a BST")
                .slug("lowest-common-ancestor-bst")
                .description("""
                    Given a BST in level-order array format, with -1 representing
                    missing nodes, and two target values p and q, print their
                    lowest common ancestor's value. Both target values exist in the tree.
                    Input: n, n level-order values, then p and q.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.TREES)
                .company(CodingCompany.MICROSOFT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            int p = sc.nextInt(), q = sc.nextInt();
                            int cur = a[0];
                            while (true) {
                                if (p < cur && q < cur) cur = a[2 * index(a, cur) + 1];
                                else if (p > cur && q > cur) cur = a[2 * index(a, cur) + 2];
                                else break;
                            }
                            System.out.println(cur);
                        }
                        static int index(int[] a, int value) {
                            for (int i = 0; i < a.length; i++)
                                if (a[i] == value) return i;
                            return -1;
                        }
                    }
                    """)
                .starterCodePython("""
                    import sys
                    v = list(map(int, sys.stdin.read().split()))
                    n = v[0]
                    a = v[1:1+n]
                    p, q = v[1+n], v[2+n]
                    cur = a[0]
                    while True:
                        if p < cur and q < cur:
                            cur = a[2*a.index(cur)+1]
                        elif p > cur and q > cur:
                            cur = a[2*a.index(cur)+2]
                        else:
                            break
                    print(cur)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        int p, q; cin >> p >> q;
                        int cur = a[0];
                        while (true) {
                            int i = 0;
                            while (a[i] != cur) i++;
                            if (p < cur && q < cur) cur = a[2*i+1];
                            else if (p > cur && q > cur) cur = a[2*i+2];
                            else break;
                        }
                        cout << cur;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], a = v.slice(1, n + 1);
                    const p = v[n + 1], q = v[n + 2];
                    let cur = a[0];
                    while (true) {
                        const i = a.indexOf(cur);
                        if (p < cur && q < cur) cur = a[2*i+1];
                        else if (p > cur && q > cur) cur = a[2*i+2];
                        else break;
                    }
                    console.log(cur);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"7\n6 2 8 0 4 7 9\n2 8", "6", "true"},
                new String[]{"7\n6 2 8 0 4 7 9\n2 4", "2", "false"},
                new String[]{"3\n2 1 3\n1 3", "2", "false"},
                new String[]{"3\n5 3 7\n3 7", "5", "false"}
        );
    }

    private void seedMergeTwoSortedLinkedLists() {
        CodingProblem problem = CodingProblem.builder()
                .title("Merge Two Sorted Linked Lists")
                .slug("merge-two-sorted-linked-lists")
                .description("""
                    Given two sorted linked lists, print their merged sorted sequence.
                    Input: n, followed by n values, then m, followed by m values.
                    Output: all merged values in ascending order, space-separated.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.LINKED_LIST)
                .company(CodingCompany.MICROSOFT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            int m = sc.nextInt();
                            int[] b = new int[m];
                            for (int i = 0; i < m; i++) b[i] = sc.nextInt();
                            int i = 0, j = 0;
                            List<Integer> result = new ArrayList<>();
                            while (i < n && j < m) {
                                if (a[i] <= b[j]) result.add(a[i++]);
                                else result.add(b[j++]);
                            }
                            while (i < n) result.add(a[i++]);
                            while (j < m) result.add(b[j++]);
                            for (int k = 0; k < result.size(); k++) {
                                if (k > 0) System.out.print(" ");
                                System.out.print(result.get(k));
                            }
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = list(map(int, input().split())) if n else []
                    m = int(input())
                    b = list(map(int, input().split())) if m else []
                    i = j = 0
                    result = []
                    while i < n and j < m:
                        if a[i] <= b[j]:
                            result.append(a[i]); i += 1
                        else:
                            result.append(b[j]); j += 1
                    result.extend(a[i:])
                    result.extend(b[j:])
                    print(*result)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        int m; cin >> m;
                        vector<int> b(m);
                        for (int &x : b) cin >> x;
                        int i = 0, j = 0;
                        vector<int> result;
                        while (i < n && j < m) {
                            if (a[i] <= b[j]) result.push_back(a[i++]);
                            else result.push_back(b[j++]);
                        }
                        while (i < n) result.push_back(a[i++]);
                        while (j < m) result.push_back(b[j++]);
                        for (int k = 0; k < (int)result.size(); k++) {
                            if (k) cout << " ";
                            cout << result[k];
                        }
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    let i = 0;
                    const n = v[i++], a = v.slice(i, i += n);
                    const m = v[i++], b = v.slice(i, i + m);
                    let x = 0, y = 0;
                    const result = [];
                    while (x < n && y < m) {
                        if (a[x] <= b[y]) result.push(a[x++]);
                        else result.push(b[y++]);
                    }
                    result.push(...a.slice(x), ...b.slice(y));
                    console.log(result.join(' '));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"3\n1 3 5\n3\n2 4 6", "1 2 3 4 5 6", "true"},
                new String[]{"2\n1 2\n2\n3 4", "1 2 3 4", "false"},
                new String[]{"0\n0", "", "false"},
                new String[]{"3\n1 2 7\n2\n3 8", "1 2 3 7 8", "false"}
        );
    }


    private void seedClimbingStairs() {
        CodingProblem problem = CodingProblem.builder()
                .title("Climbing Stairs")
                .slug("climbing-stairs")
                .description("""
                    You are climbing a staircase with n steps. You can climb either
                    1 or 2 steps at a time. Print the number of distinct ways to
                    reach the top. Assume 1 <= n <= 45.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.DP)
                .company(CodingCompany.INFOSYS)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            int n = new Scanner(System.in).nextInt();
                            long a = 1, b = 1;
                            for (int i = 2; i <= n; i++) {
                                long c = a + b;
                                a = b;
                                b = c;
                            }
                            System.out.println(n == 0 ? 1 : b);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = b = 1
                    for _ in range(2, n + 1):
                        a, b = b, a + b
                    print(1 if n == 0 else b)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        long long a = 1, b = 1;
                        for (int i = 2; i <= n; i++) {
                            long long c = a + b;
                            a = b; b = c;
                        }
                        cout << (n == 0 ? 1 : b);
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const n = Number(fs.readFileSync(0, 'utf8').trim());
                    let a = 1, b = 1;
                    for (let i = 2; i <= n; i++) {
                        [a, b] = [b, a + b];
                    }
                    console.log(n === 0 ? 1 : b);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"2", "2", "true"},
                new String[]{"3", "3", "false"},
                new String[]{"5", "8", "false"},
                new String[]{"1", "1", "false"}
        );
    }

    private void seedCountSetBits() {
        CodingProblem problem = CodingProblem.builder()
                .title("Count Set Bits")
                .slug("count-set-bits")
                .description("""
                    Given a non-negative integer n, print the number of set bits
                    (1s) in its binary representation.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.INFOSYS)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            int n = new Scanner(System.in).nextInt();
                            System.out.println(Integer.bitCount(n));
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    print(bin(n).count('1'))
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    using namespace std;
                    int main() {
                        unsigned int n; cin >> n;
                        int count = 0;
                        while (n) {
                            count += n & 1U;
                            n >>= 1;
                        }
                        cout << count;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    let n = Number(fs.readFileSync(0, 'utf8').trim());
                    let count = 0;
                    while (n > 0) {
                        count += n % 2;
                        n = Math.floor(n / 2);
                    }
                    console.log(count);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"7", "3", "true"},
                new String[]{"8", "1", "false"},
                new String[]{"15", "4", "false"},
                new String[]{"0", "0", "false"}
        );
    }

    private void seedCoinChange() {
        CodingProblem problem = CodingProblem.builder()
                .title("Coin Change")
                .slug("coin-change")
                .description("""
                    Given n coin denominations and a target amount, print the minimum
                    number of coins needed to make that amount. Each denomination can
                    be used unlimited times. Print -1 if the amount cannot be made.
                    Input: n, followed by n coin values, followed by the target amount.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.DP)
                .company(CodingCompany.INFOSYS)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] coins = new int[n];
                            for (int i = 0; i < n; i++) coins[i] = sc.nextInt();
                            int amount = sc.nextInt();
                            int[] dp = new int[amount + 1];
                            Arrays.fill(dp, amount + 1);
                            dp[0] = 0;
                            for (int coin : coins) {
                                for (int x = coin; x <= amount; x++) {
                                    dp[x] = Math.min(dp[x], dp[x - coin] + 1);
                                }
                            }
                            System.out.println(dp[amount] > amount ? -1 : dp[amount]);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    coins = list(map(int, input().split()))
                    amount = int(input())
                    dp = [amount + 1] * (amount + 1)
                    dp[0] = 0
                    for coin in coins:
                        for x in range(coin, amount + 1):
                            dp[x] = min(dp[x], dp[x - coin] + 1)
                    print(dp[amount] if dp[amount] <= amount else -1)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    #include <algorithm>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> coins(n);
                        for (int &c : coins) cin >> c;
                        int amount; cin >> amount;
                        vector<int> dp(amount + 1, amount + 1);
                        dp[0] = 0;
                        for (int coin : coins)
                            for (int x = coin; x <= amount; x++)
                                dp[x] = min(dp[x], dp[x - coin] + 1);
                        cout << (dp[amount] > amount ? -1 : dp[amount]);
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], coins = v.slice(1, n + 1), amount = v[n + 1];
                    const dp = Array(amount + 1).fill(amount + 1);
                    dp[0] = 0;
                    for (const coin of coins) {
                        for (let x = coin; x <= amount; x++) {
                            dp[x] = Math.min(dp[x], dp[x - coin] + 1);
                        }
                    }
                    console.log(dp[amount] > amount ? -1 : dp[amount]);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"3\n1 2 5\n11", "3", "true"},
                new String[]{"2\n2 4\n7", "-1", "false"},
                new String[]{"1\n1\n5", "5", "false"},
                new String[]{"3\n1 3 4\n6", "2", "false"}
        );
    }


    private void seedRotateArrayByK() {
        CodingProblem problem = CodingProblem.builder()
                .title("Rotate Array by K Positions")
                .slug("rotate-array-by-k")
                .description("""
                    Given an array of n integers and a non-negative integer k,
                    rotate the array to the right by k positions.
                    Input: n, followed by n integers, followed by k.
                    Output: the rotated array as space-separated integers.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.HCL)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            int k = sc.nextInt();
                            if (n > 0) {
                                k %= n;
                                reverse(a, 0, n - 1);
                                reverse(a, 0, k - 1);
                                reverse(a, k, n - 1);
                            }
                            for (int i = 0; i < n; i++) {
                                if (i > 0) System.out.print(" ");
                                System.out.print(a[i]);
                            }
                        }
                        static void reverse(int[] a, int l, int r) {
                            while (l < r) {
                                int t = a[l]; a[l++] = a[r]; a[r--] = t;
                            }
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = list(map(int, input().split()))
                    k = int(input())
                    if n:
                        k %= n
                        a = a[-k:] + a[:-k] if k else a
                    print(*a)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    #include <algorithm>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        int k; cin >> k;
                        if (n > 0) {
                            k %= n;
                            reverse(a.begin(), a.end());
                            reverse(a.begin(), a.begin() + k);
                            reverse(a.begin() + k, a.end());
                        }
                        for (int i = 0; i < n; i++) {
                            if (i) cout << " ";
                            cout << a[i];
                        }
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], a = v.slice(1, n + 1);
                    const kInput = v[n + 1];
                    if (n > 0) {
                        const k = kInput % n;
                        if (k) a.splice(0, n, ...a.slice(n - k), ...a.slice(0, n - k));
                    }
                    console.log(a.join(' '));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"5\n1 2 3 4 5\n2", "4 5 1 2 3", "true"},
                new String[]{"4\n1 2 3 4\n1", "4 1 2 3", "false"},
                new String[]{"3\n7 8 9\n3", "7 8 9", "false"},
                new String[]{"5\n1 2 3 4 5\n0", "1 2 3 4 5", "false"}
        );
    }

    private void seedFindDuplicateNumber() {
        CodingProblem problem = CodingProblem.builder()
                .title("Find Duplicate Number")
                .slug("find-duplicate-number")
                .description("""
                    Given an array of n integers, print any value that occurs more
                    than once. If there are no duplicates, print -1.
                    Input: n followed by n integers.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.HCL)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            Set<Integer> seen = new HashSet<>();
                            for (int i = 0; i < n; i++) {
                                int x = sc.nextInt();
                                if (!seen.add(x)) {
                                    System.out.println(x);
                                    return;
                                }
                            }
                            System.out.println(-1);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    seen = set()
                    answer = -1
                    for x in map(int, input().split()):
                        if x in seen:
                            answer = x
                            break
                        seen.add(x)
                    print(answer)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <unordered_set>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        unordered_set<int> seen;
                        for (int i = 0; i < n; i++) {
                            int x; cin >> x;
                            if (seen.count(x)) {
                                cout << x;
                                return 0;
                            }
                            seen.insert(x);
                        }
                        cout << -1;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], seen = new Set();
                    let answer = -1;
                    for (const x of v.slice(1, n + 1)) {
                        if (seen.has(x)) {
                            answer = x;
                            break;
                        }
                        seen.add(x);
                    }
                    console.log(answer);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"5\n1 3 4 2 2", "2", "true"},
                new String[]{"6\n1 2 3 4 5 3", "3", "false"},
                new String[]{"4\n1 2 3 4", "-1", "false"},
                new String[]{"5\n7 7 1 2 3", "7", "false"}
        );
    }

    private void seedMatrixDiagonalSum() {
        CodingProblem problem = CodingProblem.builder()
                .title("Matrix Diagonal Sum")
                .slug("matrix-diagonal-sum")
                .description("""
                    Given an n x n matrix, calculate the sum of its primary and
                    secondary diagonals. Count the center element only once when n is odd.
                    Input: n followed by n rows of n integers.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.HCL)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            long sum = 0;
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < n; j++) {
                                    int x = sc.nextInt();
                                    if (i == j || i + j == n - 1) sum += x;
                                }
                            }
                            System.out.println(sum);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    matrix = [list(map(int, input().split())) for _ in range(n)]
                    total = sum(matrix[i][i] for i in range(n))
                    total += sum(matrix[i][n - 1 - i] for i in range(n))
                    if n % 2:
                        total -= matrix[n // 2][n // 2]
                    print(total)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        long long sum = 0;
                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < n; j++) {
                                int x; cin >> x;
                                if (i == j || i + j == n - 1) sum += x;
                            }
                        }
                        cout << sum;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0];
                    let sum = 0;
                    for (let i = 0; i < n; i++) {
                        for (let j = 0; j < n; j++) {
                            const x = v[1 + i * n + j];
                            if (i === j || i + j === n - 1) sum += x;
                        }
                    }
                    console.log(sum);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"3\n1 2 3\n4 5 6\n7 8 9", "25", "true"},
                new String[]{"2\n1 2\n3 4", "10", "false"},
                new String[]{"1\n5", "5", "false"},
                new String[]{"3\n1 0 0\n0 2 0\n0 0 3", "6", "false"}
        );
    }

    private void seedBinarySearch() {
        CodingProblem problem = CodingProblem.builder()
                .title("Binary Search")
                .slug("binary-search")
                .description("""
                    Given a sorted array of n integers and a target, print the
                    zero-based index of the target. Print -1 if it is not found.
                    Input: n, followed by n integers, followed by the target.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.GOOGLE)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            int target = sc.nextInt();
                            int left = 0, right = n - 1, answer = -1;
                            while (left <= right) {
                                int mid = left + (right - left) / 2;
                                if (a[mid] == target) {
                                    answer = mid;
                                    break;
                                }
                                if (a[mid] < target) left = mid + 1;
                                else right = mid - 1;
                            }
                            System.out.println(answer);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = list(map(int, input().split()))
                    target = int(input())
                    left, right = 0, n - 1
                    answer = -1
                    while left <= right:
                        mid = (left + right) // 2
                        if a[mid] == target:
                            answer = mid
                            break
                        elif a[mid] < target:
                            left = mid + 1
                        else:
                            right = mid - 1
                    print(answer)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        int target; cin >> target;
                        int left = 0, right = n - 1, answer = -1;
                        while (left <= right) {
                            int mid = left + (right - left) / 2;
                            if (a[mid] == target) {
                                answer = mid; break;
                            }
                            if (a[mid] < target) left = mid + 1;
                            else right = mid - 1;
                        }
                        cout << answer;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], a = v.slice(1, n + 1), target = v[n + 1];
                    let left = 0, right = n - 1, answer = -1;
                    while (left <= right) {
                        const mid = left + Math.floor((right - left) / 2);
                        if (a[mid] === target) {
                            answer = mid; break;
                        }
                        if (a[mid] < target) left = mid + 1;
                        else right = mid - 1;
                    }
                    console.log(answer);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"5\n1 3 5 7 9\n7", "3", "true"},
                new String[]{"5\n1 2 3 4 5\n6", "-1", "false"},
                new String[]{"1\n10\n10", "0", "false"},
                new String[]{"6\n-5 -2 0 4 8 12\n-2", "1", "false"}
        );
    }

    private void seedMergeIntervals() {
        CodingProblem problem = CodingProblem.builder()
                .title("Merge Intervals")
                .slug("merge-intervals")
                .description("""
                    Given n intervals, merge all overlapping intervals.
                    Input: n followed by n pairs of start and end values.
                    Output: merged intervals in ascending order, one interval per line.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.GOOGLE)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[][] a = new int[n][2];
                            for (int i = 0; i < n; i++) {
                                a[i][0] = sc.nextInt();
                                a[i][1] = sc.nextInt();
                            }
                            Arrays.sort(a, Comparator.comparingInt(x -> x[0]));
                            List<int[]> result = new ArrayList<>();
                            for (int[] interval : a) {
                                if (result.isEmpty() ||
                                    result.get(result.size() - 1)[1] < interval[0]) {
                                    result.add(interval.clone());
                                } else {
                                    result.get(result.size() - 1)[1] =
                                        Math.max(result.get(result.size() - 1)[1], interval[1]);
                                }
                            }
                            for (int[] interval : result)
                                System.out.println(interval[0] + " " + interval[1]);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    intervals = [list(map(int, input().split())) for _ in range(n)]
                    intervals.sort()
                    result = []
                    for start, end in intervals:
                        if not result or result[-1][1] < start:
                            result.append([start, end])
                        else:
                            result[-1][1] = max(result[-1][1], end)
                    for start, end in result:
                        print(start, end)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    #include <algorithm>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<pair<int,int>> a(n);
                        for (auto &p : a) cin >> p.first >> p.second;
                        sort(a.begin(), a.end());
                        vector<pair<int,int>> result;
                        for (auto p : a) {
                            if (result.empty() || result.back().second < p.first)
                                result.push_back(p);
                            else
                                result.back().second = max(result.back().second, p.second);
                        }
                        for (auto p : result)
                            cout << p.first << " " << p.second << '\\n';
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const v = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = v[0], intervals = [];
                    for (let i = 0; i < n; i++)
                        intervals.push([v[1 + i * 2], v[2 + i * 2]]);
                    intervals.sort((a, b) => a[0] - b[0]);
                    const result = [];
                    for (const [start, end] of intervals) {
                        if (!result.length || result[result.length - 1][1] < start)
                            result.push([start, end]);
                        else
                            result[result.length - 1][1] =
                                Math.max(result[result.length - 1][1], end);
                    }
                    console.log(result.map(p => p.join(' ')).join('\\n'));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"4\n1 3\n2 6\n8 10\n15 18", "1 6\n8 10\n15 18", "true"},
                new String[]{"2\n1 4\n4 5", "1 5", "false"},
                new String[]{"1\n3 7", "3 7", "false"},
                new String[]{"3\n5 7\n1 2\n2 4", "1 4\n5 7", "false"}
        );
    }

    private void seedLongestPalindromicSubstring() {
        CodingProblem problem = CodingProblem.builder()
                .title("Longest Palindromic Substring")
                .slug("longest-palindromic-substring")
                .description("""
                    Given a string, print its longest palindromic substring.
                    If multiple answers have the same maximum length, print the
                    one that appears first in the string.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.GOOGLE)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine();
                            if (s.isEmpty()) {
                                System.out.println("");
                                return;
                            }
                            int start = 0, maxLen = 1;
                            for (int i = 0; i < s.length(); i++) {
                                for (int type = 0; type < 2; type++) {
                                    int l = i, r = i + type;
                                    while (l >= 0 && r < s.length() &&
                                           s.charAt(l) == s.charAt(r)) {
                                        int len = r - l + 1;
                                        if (len > maxLen) {
                                            start = l;
                                            maxLen = len;
                                        }
                                        l--; r++;
                                    }
                                }
                            }
                            System.out.println(s.substring(start, start + maxLen));
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input()
                    best = ""
                    for i in range(len(s)):
                        for left, right in ((i, i), (i, i + 1)):
                            while left >= 0 and right < len(s) and s[left] == s[right]:
                                candidate = s[left:right + 1]
                                if len(candidate) > len(best):
                                    best = candidate
                                left -= 1
                                right += 1
                    print(best)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    using namespace std;
                    int main() {
                        string s;
                        getline(cin, s);
                        int start = 0, maxLen = 0;
                        for (int i = 0; i < (int)s.size(); i++) {
                            for (int type = 0; type < 2; type++) {
                                int l = i, r = i + type;
                                while (l >= 0 && r < (int)s.size() && s[l] == s[r]) {
                                    if (r - l + 1 > maxLen) {
                                        start = l;
                                        maxLen = r - l + 1;
                                    }
                                    l--; r++;
                                }
                            }
                        }
                        cout << s.substr(start, maxLen);
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').replace(/\\r?\\n$/, '');
                    let start = 0, maxLen = 0;
                    for (let i = 0; i < s.length; i++) {
                        for (let type = 0; type < 2; type++) {
                            let l = i, r = i + type;
                            while (l >= 0 && r < s.length && s[l] === s[r]) {
                                if (r - l + 1 > maxLen) {
                                    start = l;
                                    maxLen = r - l + 1;
                                }
                                l--; r++;
                            }
                        }
                    }
                    console.log(s.slice(start, start + maxLen));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"babad", "bab", "true"},
                new String[]{"cbbd", "bb", "false"},
                new String[]{"racecar", "racecar", "false"},
                new String[]{"a", "a", "false"}
        );
    }


    private void seedAnagramPalindromeCheck() {
        CodingProblem problem = CodingProblem.builder()
                .title("Anagram Palindrome Check")
                .slug("anagram-palindrome-check")
                .description("""
                    Given a string, determine whether its characters can be rearranged
                    to form a palindrome. Print true or false.
                    Ignore spaces and treat uppercase and lowercase letters equally.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.COGNIZANT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine()
                                    .replaceAll("\\\\s+", "").toLowerCase();
                            Set<Character> odd = new HashSet<>();
                            for (char c : s.toCharArray()) {
                                if (!odd.add(c)) odd.remove(c);
                            }
                            System.out.println(odd.size() <= 1 ? "true" : "false");
                        }
                    }
                    """)
                .starterCodePython("""
                    s = ''.join(input().lower().split())
                    odd = set()
                    for c in s:
                        if c in odd:
                            odd.remove(c)
                        else:
                            odd.add(c)
                    print("true" if len(odd) <= 1 else "false")
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    #include <unordered_set>
                    #include <cctype>
                    using namespace std;
                    int main() {
                        string s;
                        getline(cin, s);
                        unordered_set<char> odd;
                        for (unsigned char c : s) {
                            if (isspace(c)) continue;
                            c = tolower(c);
                            if (odd.count(c)) odd.erase(c);
                            else odd.insert(c);
                        }
                        cout << (odd.size() <= 1 ? "true" : "false");
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').trim().toLowerCase().replace(/\\s/g, '');
                    const odd = new Set();
                    for (const c of s) {
                        if (odd.has(c)) odd.delete(c);
                        else odd.add(c);
                    }
                    console.log(odd.size <= 1 ? 'true' : 'false');
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"carrace", "true", "true"},
                new String[]{"hello", "false", "false"},
                new String[]{"Tact Coa", "true", "false"},
                new String[]{"aabb", "true", "false"}
        );
    }

    private void seedFindMissingCharacter() {
        CodingProblem problem = CodingProblem.builder()
                .title("Find the Missing Character")
                .slug("find-missing-character")
                .description("""
                    Given a string containing distinct lowercase English letters from
                    the alphabet, print the first missing letter in alphabetical order.
                    If no letter is missing, print -1.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.COGNIZANT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine();
                            boolean[] seen = new boolean[26];
                            for (char c : s.toCharArray()) {
                                if (c >= 'a' && c <= 'z') seen[c - 'a'] = true;
                            }
                            for (int i = 0; i < 26; i++) {
                                if (!seen[i]) {
                                    System.out.println((char)('a' + i));
                                    return;
                                }
                            }
                            System.out.println(-1);
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input()
                    for c in 'abcdefghijklmnopqrstuvwxyz':
                        if c not in s:
                            print(c)
                            break
                    else:
                        print(-1)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    using namespace std;
                    int main() {
                        string s;
                        getline(cin, s);
                        bool seen[26] = {};
                        for (char c : s)
                            if (c >= 'a' && c <= 'z') seen[c - 'a'] = true;
                        for (int i = 0; i < 26; i++) {
                            if (!seen[i]) {
                                cout << char('a' + i);
                                return 0;
                            }
                        }
                        cout << -1;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').trim();
                    let answer = -1;
                    for (let code = 97; code <= 122; code++) {
                        if (!s.includes(String.fromCharCode(code))) {
                            answer = String.fromCharCode(code);
                            break;
                        }
                    }
                    console.log(answer);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"abcde", "f", "true"},
                new String[]{"abcdefghijklmnopqrstuvwxyz", "-1", "false"},
                new String[]{"abdef", "c", "false"},
                new String[]{"xyz", "a", "false"}
        );
    }

    private void seedCountWordsInString() {
        CodingProblem problem = CodingProblem.builder()
                .title("Count Words in a String")
                .slug("count-words-in-string")
                .description("""
                    Given a line of text, count the number of words.
                    Words are separated by one or more whitespace characters.
                    An empty line contains zero words.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.COGNIZANT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine().trim();
                            System.out.println(s.isEmpty() ? 0 : s.split("\\\\s+").length);
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input().split()
                    print(len(s))
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <sstream>
                    #include <string>
                    using namespace std;
                    int main() {
                        string s, word;
                        getline(cin, s);
                        stringstream ss(s);
                        int count = 0;
                        while (ss >> word) count++;
                        cout << count;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').trim();
                    console.log(s ? s.split(/\\s+/).length : 0);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"Hello world from Java", "4", "true"},
                new String[]{"PrepWise AI", "2", "false"},
                new String[]{"one   two    three", "3", "false"},
                new String[]{"", "0", "false"}
        );
    }

    private void seedSecondMostFrequentCharacter() {
        CodingProblem problem = CodingProblem.builder()
                .title("Second Most Frequent Character")
                .slug("second-most-frequent-character")
                .description("""
                    Given a string, print the character with the second-highest frequency.
                    If multiple characters have the same frequency, choose the character
                    that appears first among them in the input. If no second frequency
                    exists, print -1.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.CAPGEMINI)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine();
                            Map<Character, Integer> freq = new LinkedHashMap<>();
                            for (char c : s.toCharArray())
                                freq.put(c, freq.getOrDefault(c, 0) + 1);
                            List<Integer> counts = new ArrayList<>(new HashSet<>(freq.values()));
                            counts.sort(Collections.reverseOrder());
                            if (counts.size() < 2) {
                                System.out.println(-1);
                                return;
                            }
                            int target = counts.get(1);
                            for (char c : s.toCharArray()) {
                                if (freq.get(c) == target) {
                                    System.out.println(c);
                                    return;
                                }
                            }
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input()
                    freq = {}
                    for c in s:
                        freq[c] = freq.get(c, 0) + 1
                    counts = sorted(set(freq.values()), reverse=True)
                    if len(counts) < 2:
                        print(-1)
                    else:
                        target = counts[1]
                        print(next(c for c in s if freq[c] == target))
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    #include <unordered_map>
                    #include <set>
                    using namespace std;
                    int main() {
                        string s;
                        getline(cin, s);
                        unordered_map<char, int> freq;
                        for (char c : s) freq[c]++;
                        set<int, greater<int>> counts;
                        for (auto p : freq) counts.insert(p.second);
                        if (counts.size() < 2) {
                            cout << -1;
                            return 0;
                        }
                        auto it = counts.begin();
                        ++it;
                        int target = *it;
                        for (char c : s) {
                            if (freq[c] == target) {
                                cout << c;
                                return 0;
                            }
                        }
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').replace(/\\r?\\n$/, '');
                    const freq = new Map();
                    for (const c of s) freq.set(c, (freq.get(c) || 0) + 1);
                    const counts = [...new Set(freq.values())].sort((a, b) => b - a);
                    if (counts.length < 2) {
                        console.log(-1);
                    } else {
                        const target = counts[1];
                        console.log([...s].find(c => freq.get(c) === target));
                    }
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"aabbbcccc", "b", "true"},
                new String[]{"aabbc", "b", "false"},
                new String[]{"aaaa", "-1", "false"},
                new String[]{"banana", "b", "false"}
        );
    }

    private void seedSort012Array() {
        CodingProblem problem = CodingProblem.builder()
                .title("Sort an Array of 0s, 1s and 2s")
                .slug("sort-012-array")
                .description("""
                    Given an array containing only 0, 1 and 2, sort it in ascending order.
                    Input: n followed by n integers.
                    Output: the sorted array as space-separated integers.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.CAPGEMINI)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            int low = 0, mid = 0, high = n - 1;
                            while (mid <= high) {
                                if (a[mid] == 0) {
                                    int t = a[low]; a[low++] = a[mid]; a[mid++] = t;
                                } else if (a[mid] == 1) {
                                    mid++;
                                } else {
                                    int t = a[mid]; a[mid] = a[high]; a[high--] = t;
                                }
                            }
                            for (int i = 0; i < n; i++) {
                                if (i > 0) System.out.print(" ");
                                System.out.print(a[i]);
                            }
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = list(map(int, input().split()))
                    low = mid = 0
                    high = n - 1
                    while mid <= high:
                        if a[mid] == 0:
                            a[low], a[mid] = a[mid], a[low]
                            low += 1
                            mid += 1
                        elif a[mid] == 1:
                            mid += 1
                        else:
                            a[mid], a[high] = a[high], a[mid]
                            high -= 1
                    print(*a)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        int low = 0, mid = 0, high = n - 1;
                        while (mid <= high) {
                            if (a[mid] == 0) swap(a[low++], a[mid++]);
                            else if (a[mid] == 1) mid++;
                            else swap(a[mid], a[high--]);
                        }
                        for (int i = 0; i < n; i++) {
                            if (i) cout << " ";
                            cout << a[i];
                        }
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const input = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = input[0];
                    const a = input.slice(1, n + 1);
                    let low = 0, mid = 0, high = n - 1;
                    while (mid <= high) {
                        if (a[mid] === 0) {
                            [a[low], a[mid]] = [a[mid], a[low]];
                            low++; mid++;
                        } else if (a[mid] === 1) {
                            mid++;
                        } else {
                            [a[mid], a[high]] = [a[high], a[mid]];
                            high--;
                        }
                    }
                    console.log(a.join(' '));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"6\n0 2 1 2 0 1", "0 0 1 1 2 2", "true"},
                new String[]{"3\n2 0 1", "0 1 2", "false"},
                new String[]{"4\n0 0 0 0", "0 0 0 0", "false"},
                new String[]{"5\n2 2 1 1 0", "0 1 1 2 2", "false"}
        );
    }

    private void seedRemoveSpacesFromString() {
        CodingProblem problem = CodingProblem.builder()
                .title("Remove Spaces from a String")
                .slug("remove-spaces-from-string")
                .description("""
                    Given a line of text, remove all space characters and print the result.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.CAPGEMINI)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine();
                            System.out.println(s.replace(" ", ""));
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input()
                    print(s.replace(" ", ""))
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    using namespace std;
                    int main() {
                        string s, result;
                        getline(cin, s);
                        for (char c : s) if (c != ' ') result += c;
                        cout << result;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').replace(/\\r?\\n$/, '');
                    console.log(s.replace(/ /g, ''));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"hello world", "helloworld", "true"},
                new String[]{"Java Full Stack", "JavaFullStack", "false"},
                new String[]{"no spaces", "nospaces", "false"},
                new String[]{"abc", "abc", "false"}
        );
    }


    private void seedLongestSubstringWithoutRepeatingCharacters() {
        CodingProblem problem = CodingProblem.builder()
                .title("Longest Substring Without Repeating Characters")
                .slug("longest-substring-without-repeating-characters")
                .description("""
                    Given a string s, print the length of the longest substring
                    that contains no repeated characters.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.AMAZON)

                .memoryLimitMb(256)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            String s = sc.nextLine();
                            Map<Character, Integer> lastSeen = new HashMap<>();
                            int left = 0, maxLength = 0;
                            for (int right = 0; right < s.length(); right++) {
                                char c = s.charAt(right);
                                if (lastSeen.containsKey(c)) {
                                    left = Math.max(left, lastSeen.get(c) + 1);
                                }
                                lastSeen.put(c, right);
                                maxLength = Math.max(maxLength, right - left + 1);
                            }
                            System.out.println(maxLength);
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input()
                    last_seen = {}
                    left = max_length = 0
                    for right, char in enumerate(s):
                        if char in last_seen:
                            left = max(left, last_seen[char] + 1)
                        last_seen[char] = right
                        max_length = max(max_length, right - left + 1)
                    print(max_length)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    #include <unordered_map>
                    #include <algorithm>
                    using namespace std;
                    int main() {
                        string s;
                        getline(cin, s);
                        unordered_map<char, int> lastSeen;
                        int left = 0, maxLength = 0;
                        for (int right = 0; right < (int)s.size(); right++) {
                            char c = s[right];
                            if (lastSeen.count(c)) {
                                left = max(left, lastSeen[c] + 1);
                            }
                            lastSeen[c] = right;
                            maxLength = max(maxLength, right - left + 1);
                        }
                        cout << maxLength;
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').replace(/\\r?\\n$/, '');
                    const lastSeen = new Map();
                    let left = 0, maxLength = 0;
                    for (let right = 0; right < s.length; right++) {
                        const c = s[right];
                        if (lastSeen.has(c)) {
                            left = Math.max(left, lastSeen.get(c) + 1);
                        }
                        lastSeen.set(c, right);
                        maxLength = Math.max(maxLength, right - left + 1);
                    }
                    console.log(maxLength);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"abcabcbb", "3", "true"},
                new String[]{"bbbbb", "1", "false"},
                new String[]{"pwwkew", "3", "false"},
                new String[]{"", "0", "false"}
        );
    }

    private void seedProductOfArrayExceptSelf() {
        CodingProblem problem = CodingProblem.builder()
                .title("Product of Array Except Self")
                .slug("product-of-array-except-self")
                .description("""
                    Given an array of n integers, print an output array where each
                    element is the product of all input elements except itself.
                    Do not use division.
                    Input: n followed by n integers.
                    Output: n space-separated integers.
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.AMAZON)

                .memoryLimitMb(256)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            long[] a = new long[n];
                            long[] result = new long[n];
                            long prefix = 1;
                            for (int i = 0; i < n; i++) a[i] = sc.nextLong();
                            for (int i = 0; i < n; i++) {
                                result[i] = prefix;
                                prefix *= a[i];
                            }
                            long suffix = 1;
                            for (int i = n - 1; i >= 0; i--) {
                                result[i] *= suffix;
                                suffix *= a[i];
                            }
                            for (int i = 0; i < n; i++) {
                                if (i > 0) System.out.print(" ");
                                System.out.print(result[i]);
                            }
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    nums = list(map(int, input().split()))
                    result = [1] * n
                    prefix = 1
                    for i in range(n):
                        result[i] = prefix
                        prefix *= nums[i]
                    suffix = 1
                    for i in range(n - 1, -1, -1):
                        result[i] *= suffix
                        suffix *= nums[i]
                    print(*result)
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <vector>
                    using namespace std;
                    int main() {
                        int n;
                        cin >> n;
                        vector<long long> a(n), result(n, 1);
                        for (int i = 0; i < n; i++) cin >> a[i];
                        long long prefix = 1;
                        for (int i = 0; i < n; i++) {
                            result[i] = prefix;
                            prefix *= a[i];
                        }
                        long long suffix = 1;
                        for (int i = n - 1; i >= 0; i--) {
                            result[i] *= suffix;
                            suffix *= a[i];
                        }
                        for (int i = 0; i < n; i++) {
                            if (i > 0) cout << " ";
                            cout << result[i];
                        }
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const input = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = input[0];
                    const nums = input.slice(1, n + 1);
                    const result = Array(n).fill(1);
                    let prefix = 1;
                    for (let i = 0; i < n; i++) {
                        result[i] = prefix;
                        prefix *= nums[i];
                    }
                    let suffix = 1;
                    for (let i = n - 1; i >= 0; i--) {
                        result[i] *= suffix;
                        suffix *= nums[i];
                    }
                    console.log(result.join(' '));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"4\n1 2 3 4", "24 12 8 6", "true"},
                new String[]{"3\n2 3 4", "12 8 6", "false"},
                new String[]{"4\n-1 1 0 -3", "0 0 3 0", "false"},
                new String[]{"1\n5", "1", "false"}
        );
    }


    private void seedReverseANumber() {
        CodingProblem problem = CodingProblem.builder()
                .title("Reverse a Number")
                .slug("reverse-a-number")
                .description("""
                    Given an integer n, reverse its digits and print the result.
                    Preserve the sign for negative numbers.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.ACCENTURE)

                .memoryLimitMb(256)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            long num = Math.abs((long) n);
                            long rev = 0;
                            while (num > 0) {
                                rev = rev * 10 + num % 10;
                                num /= 10;
                            }
                            System.out.println(n < 0 ? -rev : rev);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    sign = -1 if n < 0 else 1
                    print(sign * int(str(abs(n))[::-1]))
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    #include <algorithm>
                    using namespace std;
                    int main() {
                        long long n;
                        cin >> n;
                        string s = to_string(n);
                        if (s[0] == '-') {
                            reverse(s.begin() + 1, s.end());
                        } else {
                            reverse(s.begin(), s.end());
                        }
                        cout << stoll(s);
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const n = Number(fs.readFileSync(0, 'utf8').trim());
                    const sign = n < 0 ? -1 : 1;
                    console.log(sign * Number(String(Math.abs(n)).split('').reverse().join('')));
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"1234", "4321", "true"},
                new String[]{"1200", "21", "false"},
                new String[]{"-456", "-654", "false"},
                new String[]{"7", "7", "false"}
        );
    }

    private void seedGCDOfTwoNumbers() {
        CodingProblem problem = CodingProblem.builder()
                .title("GCD of Two Numbers")
                .slug("gcd-of-two-numbers")
                .description("""
                    Given two integers a and b, print their greatest common divisor (GCD).
                    The GCD is always non-negative.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.RECURSION)
                .company(CodingCompany.ACCENTURE)

                .memoryLimitMb(256)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            long a = Math.abs(sc.nextLong());
                            long b = Math.abs(sc.nextLong());
                            while (b != 0) {
                                long temp = b;
                                b = a % b;
                                a = temp;
                            }
                            System.out.println(a);
                        }
                    }
                    """)
                .starterCodePython("""
                    import math
                    a, b = map(int, input().split())
                    print(math.gcd(a, b))
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <numeric>
                    #include <cstdlib>
                    using namespace std;
                    int main() {
                        long long a, b;
                        cin >> a >> b;
                        cout << gcd(llabs(a), llabs(b));
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    let [a, b] = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    a = Math.abs(a);
                    b = Math.abs(b);
                    while (b !== 0) {
                        const temp = b;
                        b = a % b;
                        a = temp;
                    }
                    console.log(a);
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"12 18", "6", "true"},
                new String[]{"7 13", "1", "false"},
                new String[]{"24 36", "12", "false"},
                new String[]{"10 0", "10", "false"}
        );
    }

    private void seedArmstrongNumber() {
        CodingProblem problem = CodingProblem.builder()
                .title("Armstrong Number")
                .slug("armstrong-number")
                .description("""
                    Given a non-negative integer n, determine whether it is an Armstrong number.
                    An Armstrong number equals the sum of its digits, each raised to the
                    power of the number of digits. Print true or false.
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.ACCENTURE)

                .memoryLimitMb(256)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            String s = String.valueOf(n);
                            long sum = 0;
                            for (char c : s.toCharArray()) {
                                sum += (long) Math.pow(c - '0', s.length());
                            }
                            System.out.println(sum == n ? "true" : "false");
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    digits = str(n)
                    total = sum(int(d) ** len(digits) for d in digits)
                    print("true" if total == n else "false")
                    """)
                .starterCodeCpp("""
                    #include <iostream>
                    #include <string>
                    #include <cmath>
                    using namespace std;
                    int main() {
                        int n;
                        cin >> n;
                        string s = to_string(n);
                        long long sum = 0;
                        for (char c : s) {
                            sum += (long long)pow(c - '0', s.length());
                        }
                        cout << (sum == n ? "true" : "false");
                        return 0;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const n = Number(fs.readFileSync(0, 'utf8').trim());
                    const digits = String(n);
                    const sum = [...digits].reduce((total, d) => total + Number(d) ** digits.length, 0);
                    console.log(sum === n ? 'true' : 'false');
                    """)
                .build();

        problemRepository.save(problem);

        saveTestCases(problem,
                new String[]{"153", "true", "true"},
                new String[]{"123", "false", "false"},
                new String[]{"9474", "true", "false"},
                new String[]{"0", "true", "false"}
        );
    }


    private void seedSecondLargestElement() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Second Largest Element")
                .slug("second-largest-element")
                .description("""
                Given an array of integers, find the second-largest
                distinct element. Print -1 if it does not exist.

                Input:
                Line 1: n
                Line 2: n space-separated integers

                Output: The second-largest distinct element, or -1.
                """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.ACCENTURE)
                .starterCodeJava("""
                        
                        import java.util.*;
                        public class Main {
                            public static void main(String[] args) {
                                Scanner sc = new Scanner(System.in);
                                int n = sc.nextInt();
                                TreeSet<Integer> values = new TreeSet<>();
                                for (int i = 0; i < n; i++) {
                                    values.add(sc.nextInt());
                                }
                                if (values.size() < 2) {
                                    System.out.println(-1);
                                } else {
                                    values.pollLast();
                                    System.out.println(values.last());
                                }
                            }
                        }
                        
                """)
                .starterCodePython("""
                n = int(input())
                a = list(map(int, input().split()))[:n]
                unique = sorted(set(a), reverse=True)
                print(unique[1] if len(unique) >= 2 else -1)
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int main() {
                    int n; cin >> n;
                    set<int, greater<int>> values;
                    for (int i = 0; i < n; i++) {
                        int x; cin >> x;
                        values.insert(x);
                    }
                    if (values.size() < 2) cout << -1;
                    else {
                        auto it = values.begin();
                        ++it;
                        cout << *it;
                    }
                }
                """)
                .starterCodeJavascript("""
                const fs = require('fs');
                const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                const n = a[0];
                const unique = [...new Set(a.slice(1, n + 1))].sort((x, y) => y - x);
                console.log(unique.length >= 2 ? unique[1] : -1);
                """)
                .build());

        saveTestCases(problem,
                new String[]{"5\\n12 35 1 10 34", "34", "true"},
                new String[]{"4\\n10 10 9 9", "9", "false"},
                new String[]{"3\\n-5 -2 -9", "-5", "false"},
                new String[]{"3\\n7 7 7", "-1", "false"});
    }

    private void seedAnagramCheck() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Valid Anagram")
                .slug("anagram-check")
                .description("""
                Given two strings without spaces, determine whether
                they are anagrams. Ignore letter case.

                Input: Two strings on separate lines.
                Output: Print true if they are anagrams, otherwise false.
                """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.CAPGEMINI)
                .starterCodeJava("""
                import java.util.*;
                public class Main {
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        char[] a = sc.next().toLowerCase().toCharArray();
                        char[] b = sc.next().toLowerCase().toCharArray();
                        Arrays.sort(a);
                        Arrays.sort(b);
                        System.out.println(Arrays.equals(a, b));
                    }
                }
                """)
                .starterCodePython("""
                a = input().lower()
                b = input().lower()
                print('true' if sorted(a) == sorted(b) else 'false')
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int main() {
                    string a, b;
                    cin >> a >> b;
                    transform(a.begin(), a.end(), a.begin(), ::tolower);
                    transform(b.begin(), b.end(), b.begin(), ::tolower);
                    sort(a.begin(), a.end());
                    sort(b.begin(), b.end());
                    cout << (a == b ? "true" : "false");
                }
                """)
                .starterCodeJavascript("""
                const fs = require('fs');
                const a = fs.readFileSync(0, 'utf8').trim().toLowerCase().split(/\\s+/);
                const sort = s => s.split('').sort().join('');
                console.log(sort(a[0]) === sort(a[1]) ? 'true' : 'false');
                """)
                .build());

        saveTestCases(problem,
                new String[]{"listen\\nsilent", "true", "true"},
                new String[]{"hello\\nworld", "false", "false"},
                new String[]{"Dormitory\\nDirtyroom", "true", "false"},
                new String[]{"aab\\nabb", "false", "false"});
    }

    private void seedStringReversal() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Reverse a String")
                .slug("string-reversal")
                .description("""
                Given a string without spaces, print the string
                in reverse order.

                Input: One string.
                Output: The reversed string.
                """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.COGNIZANT)
                .starterCodeJava("""
                import java.util.*;
                public class Main {
                    public static void main(String[] args) {
                        String s = new Scanner(System.in).next();
                        System.out.println(new StringBuilder(s).reverse());
                    }
                }
                """)
                .starterCodePython("""
                s = input().strip()
                print(s[::-1])
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int main() {
                    string s; cin >> s;
                    reverse(s.begin(), s.end());
                    cout << s;
                }
                """)
                .starterCodeJavascript("""
                const fs = require('fs');
                const s = fs.readFileSync(0, 'utf8').trim();
                console.log(s.split('').reverse().join(''));
                """)
                .build());

        saveTestCases(problem,
                new String[]{"hello", "olleh", "true"},
                new String[]{"a", "a", "false"},
                new String[]{"level", "level", "false"},
                new String[]{"placement", "tnemecalp", "false"});
    }

    private void seedMissingNumber() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Missing Number")
                .slug("missing-number")
                .description("""
                An array contains n-1 distinct numbers from 1 to n.
                Find the one missing number.

                Input:
                Line 1: n
                Line 2: n-1 space-separated integers

                Output: The missing number.
                """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.HCL)
                .starterCodeJava("""
                import java.util.*;
                public class Main {
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        int n = sc.nextInt();
                        long expected = (long) n * (n + 1) / 2;
                        long sum = 0;
                        for (int i = 0; i < n - 1; i++) {
                            sum += sc.nextInt();
                        }
                        System.out.println(expected - sum);
                    }
                }
                """)
                .starterCodePython("""
                n = int(input())
                a = list(map(int, input().split()))
                print(n * (n + 1) // 2 - sum(a))
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int main() {
                    long long n; cin >> n;
                    long long sum = 0;
                    for (int i = 0; i < n - 1; i++) {
                        long long x; cin >> x;
                        sum += x;
                    }
                    cout << n * (n + 1) / 2 - sum;
                }
                """)
                .starterCodeJavascript("""
                const fs = require('fs');
                const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                const n = a[0];
                const sum = a.slice(1, n).reduce((total, x) => total + x, 0);
                console.log(n * (n + 1) / 2 - sum);
                """)
                .build());

        saveTestCases(problem,
                new String[]{"5\\n1 2 4 5", "3", "true"},
                new String[]{"4\\n1 2 3", "4", "false"},
                new String[]{"6\\n1 2 3 4 6", "5", "false"},
                new String[]{"2\\n2", "1", "false"});
    }

    private void seedRemoveDuplicatesArray() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Remove Duplicates from Array")
                .slug("remove-duplicates-array")
                .description("""
                Given an array, remove duplicate values while preserving
                the order of their first occurrence.

                Input:
                Line 1: n
                Line 2: n space-separated integers

                Output: The distinct values separated by spaces.
                """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.AMAZON)
                .starterCodeJava("""
                import java.util.*;
                public class Main {
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        int n = sc.nextInt();
                        Set<Integer> seen = new LinkedHashSet<>();
                        for (int i = 0; i < n; i++) {
                            seen.add(sc.nextInt());
                        }
                        StringJoiner result = new StringJoiner(" ");
                        for (int x : seen) result.add(String.valueOf(x));
                        System.out.println(result);
                    }
                }
                """)
                .starterCodePython("""
                n = int(input())
                a = list(map(int, input().split()))[:n]
                print(*dict.fromkeys(a))
                """)
                .starterCodeCpp("""
                #include <bits/stdc++.h>
                using namespace std;
                int main() {
                    int n; cin >> n;
                    unordered_set<int> seen;
                    bool first = true;
                    for (int i = 0; i < n; i++) {
                        int x; cin >> x;
                        if (seen.insert(x).second) {
                            if (!first) cout << " ";
                            cout << x;
                            first = false;
                        }
                    }
                }
                """)
                .starterCodeJavascript("""
                const fs = require('fs');
                const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                const n = a[0];
                console.log([...new Set(a.slice(1, n + 1))].join(' '));
                """)
                .build());

        saveTestCases(problem,
                new String[]{"7\\n1 2 2 3 1 4 3", "1 2 3 4", "true"},
                new String[]{"5\\n5 5 5 5 5", "5", "false"},
                new String[]{"6\\n0 -1 0 2 -1 3", "0 -1 2 3", "false"},
                new String[]{"4\\n1 2 3 4", "1 2 3 4", "false"});
    }

    private void seedBestTimeStock() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Best Time to Buy and Sell Stock")
                .slug("best-time-stock")
                .description("""
                    Given stock prices for consecutive days, find the maximum
                    profit from one buy followed by one sell. If no profit
                    is possible, print 0.

                    Input:
                    Line 1: n
                    Line 2: n space-separated prices

                    Output: Maximum profit.

                    Example:
                    Input:
                    6
                    7 1 5 3 6 4
                    Output:
                    5
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.AMAZON)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt(), min = Integer.MAX_VALUE, profit = 0;
                            for (int i = 0; i < n; i++) {
                                int price = sc.nextInt();
                                min = Math.min(min, price);
                                profit = Math.max(profit, price - min);
                            }
                            System.out.println(profit);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    prices = list(map(int, input().split()))
                    minimum = float('inf')
                    profit = 0
                    for price in prices[:n]:
                        minimum = min(minimum, price)
                        profit = max(profit, price - minimum)
                    print(profit)
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        int mn = INT_MAX, profit = 0;
                        for (int i = 0; i < n; i++) {
                            int price; cin >> price;
                            mn = min(mn, price);
                            profit = max(profit, price - mn);
                        }
                        cout << profit;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = a[0];
                    let minPrice = Infinity, profit = 0;
                    for (let i = 1; i <= n; i++) {
                        minPrice = Math.min(minPrice, a[i]);
                        profit = Math.max(profit, a[i] - minPrice);
                    }
                    console.log(profit);
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"6\n7 1 5 3 6 4", "5", "true"},
                new String[]{"5\n7 6 4 3 1", "0", "false"},
                new String[]{"1\n5", "0", "false"},
                new String[]{"5\n1 2 3 4 5", "4", "false"});
    }

    private void seedFirstNonRepeatingCharacter() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("First Non-Repeating Character")
                .slug("first-non-repeating-character")
                .description("""
                    Given a string without spaces, print its first character
                    that occurs exactly once. Print -1 if no such character exists.
                    Characters are case-sensitive.

                    Input: One string.
                    Output: The first non-repeating character, or -1.

                    Example:
                    Input: leetcode
                    Output: l
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.GOOGLE)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).next();
                            Map<Character, Integer> freq = new LinkedHashMap<>();
                            for (char c : s.toCharArray())
                                freq.put(c, freq.getOrDefault(c, 0) + 1);
                            for (char c : s.toCharArray()) {
                                if (freq.get(c) == 1) {
                                    System.out.println(c);
                                    return;
                                }
                            }
                            System.out.println(-1);
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input().strip()
                    for c in s:
                        if s.count(c) == 1:
                            print(c)
                            break
                    else:
                        print(-1)
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        string s; cin >> s;
                        int freq[256] = {};
                        for (unsigned char c : s) freq[c]++;
                        for (unsigned char c : s) {
                            if (freq[c] == 1) {
                                cout << c;
                                return 0;
                            }
                        }
                        cout << -1;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').trim();
                    let answer = -1;
                    for (const c of s) {
                        if (s.indexOf(c) === s.lastIndexOf(c)) {
                            answer = c;
                            break;
                        }
                    }
                    console.log(answer);
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"leetcode", "l", "true"},
                new String[]{"aabbc", "c", "false"},
                new String[]{"aabb", "-1", "false"},
                new String[]{"z", "z", "false"});
    }

    private void seedSumOfDigits() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Sum of Digits")
                .slug("sum-of-digits")
                .description("""
                    Given an integer n, print the sum of its decimal digits.
                    For negative numbers, ignore the minus sign.

                    Input: One integer n.
                    Output: The sum of its digits.

                    Example:
                    Input: 1234
                    Output: 10
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.RECURSION)
                .company(CodingCompany.INFOSYS)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            long n = Math.abs(new Scanner(System.in).nextLong());
                            int sum = 0;
                            while (n > 0) {
                                sum += n % 10;
                                n /= 10;
                            }
                            System.out.println(sum);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = abs(int(input()))
                    total = 0
                    while n > 0:
                        total += n % 10
                        n //= 10
                    print(total)
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        long long n; cin >> n;
                        n = abs(n);
                        int sum = 0;
                        while (n > 0) {
                            sum += n % 10;
                            n /= 10;
                        }
                        cout << sum;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    let n = Math.abs(Number(fs.readFileSync(0, 'utf8').trim()));
                    let sum = 0;
                    while (n > 0) {
                        sum += n % 10;
                        n = Math.floor(n / 10);
                    }
                    console.log(sum);
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"1234", "10", "true"},
                new String[]{"0", "0", "true"},
                new String[]{"999", "27", "false"},
                new String[]{"-123", "6", "false"});
    }

    private void seedBinaryTreeLevelOrder() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Binary Tree Level Order Traversal")
                .slug("binary-tree-level-order")
                .description("""
                    A complete binary tree is represented by its level-order
                    array of n integers. Print each tree level on a separate
                    line. The root is the first value; children follow in
                    array order at indices 2*i+1 and 2*i+2.

                    Input:
                    Line 1: n
                    Line 2: n space-separated integers

                    Output: One line per level, values separated by spaces.

                    Example:
                    Input:
                    7
                    1 2 3 4 5 6 7
                    Output:
                    1
                    2 3
                    4 5 6 7
                    """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.TREES)
                .company(CodingCompany.MICROSOFT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            for (int start = 0, size = 1; start < n;
                                 start += size, size *= 2) {
                                int end = Math.min(n, start + size);
                                for (int i = start; i < end; i++) {
                                    if (i > start) System.out.print(" ");
                                    System.out.print(a[i]);
                                }
                                System.out.println();
                            }
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = list(map(int, input().split()))[:n]
                    start, size = 0, 1
                    while start < n:
                        print(*a[start:min(n, start + size)])
                        start += size
                        size *= 2
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        for (int start = 0, size = 1; start < n;
                             start += size, size *= 2) {
                            int end = min(n, start + size);
                            for (int i = start; i < end; i++) {
                                if (i > start) cout << " ";
                                cout << a[i];
                            }
                            cout << "\\n";
                        }
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = a[0];
                    let start = 0, size = 1;
                    while (start < n) {
                        console.log(a.slice(start + 1, Math.min(n, start + size) + 1).join(' '));
                        start += size;
                        size *= 2;
                    }
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"7\n1 2 3 4 5 6 7", "1\n2 3\n4 5 6 7", "true"},
                new String[]{"1\n9", "9", "false"},
                new String[]{"3\n10 20 30", "10\n20 30", "false"},
                new String[]{"5\n1 2 3 4 5", "1\n2 3\n4 5", "false"});
    }

    private void seedMoveZerosToEnd() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Move Zeros to End")
                .slug("move-zeros-to-end")
                .description("""
                    Given an array of integers, move all zeros to the end
                    while preserving the relative order of non-zero elements.

                    Input:
                    Line 1: n
                    Line 2: n space-separated integers

                    Output: The rearranged array separated by spaces.

                    Example:
                    Input:
                    5
                    0 1 0 3 12
                    Output:
                    1 3 12 0 0
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.TCS)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int[] a = new int[n];
                            int k = 0;
                            for (int i = 0; i < n; i++) a[i] = sc.nextInt();
                            for (int x : a) if (x != 0) a[k++] = x;
                            while (k < n) a[k++] = 0;
                            for (int i = 0; i < n; i++) {
                                if (i > 0) System.out.print(" ");
                                System.out.print(a[i]);
                            }
                            System.out.println();
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    a = list(map(int, input().split()))[:n]
                    result = [x for x in a if x != 0]
                    result += [0] * (n - len(result))
                    print(*result)
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        vector<int> a(n);
                        for (int &x : a) cin >> x;
                        int k = 0;
                        for (int x : a) if (x != 0) a[k++] = x;
                        while (k < n) a[k++] = 0;
                        for (int i = 0; i < n; i++) {
                            if (i) cout << " ";
                            cout << a[i];
                        }
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = a[0];
                    const values = a.slice(1, n + 1);
                    const result = values.filter(x => x !== 0);
                    while (result.length < n) result.push(0);
                    console.log(result.join(' '));
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"5\n0 1 0 3 12", "1 3 12 0 0", "true"},
                new String[]{"4\n0 0 0 0", "0 0 0 0", "false"},
                new String[]{"3\n1 2 3", "1 2 3", "false"},
                new String[]{"5\n0 -1 0 2 -3", "-1 2 -3 0 0", "false"});
    }


    private void seedPalindromeNumber() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Palindrome Number")
                .slug("palindrome-number")
                .description("""
                    Given an integer, determine whether it reads the same
                    forward and backward. Negative numbers are not palindromes.

                    Input format:
                    A single integer n.

                    Output format:
                    Print true if n is a palindrome, otherwise print false.

                    Example:
                    Input: 121
                    Output: true
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.COGNIZANT)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            int n = new Scanner(System.in).nextInt();
                            String s = String.valueOf(n);
                            System.out.println(n >= 0 &&
                                s.equals(new StringBuilder(s).reverse().toString()));
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    print(n >= 0 and str(n) == str(n)[::-1])
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        long long n; cin >> n;
                        string s = to_string(n), r = s;
                        reverse(r.begin(), r.end());
                        cout << (n >= 0 && s == r ? "true" : "false");
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const n = Number(fs.readFileSync(0, 'utf8').trim());
                    const s = String(n);
                    console.log(n >= 0 && s === [...s].reverse().join('')
                        ? 'true' : 'false');
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"121", "true", "true"},
                new String[]{"123", "false", "true"},
                new String[]{"0", "true", "false"},
                new String[]{"-121", "false", "false"});
    }

    private void seedCountVowels() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Count Vowels")
                .slug("count-vowels")
                .description("""
                    Given a line of text, count the vowels (a, e, i, o, u).
                    Both uppercase and lowercase vowels count.

                    Input format:
                    A single line of text.

                    Output format:
                    Print the number of vowels.

                    Example:
                    Input: Hello World
                    Output: 3
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.CAPGEMINI)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            String s = new Scanner(System.in).nextLine().toLowerCase();
                            int count = 0;
                            for (char c : s.toCharArray())
                                if ("aeiou".indexOf(c) >= 0) count++;
                            System.out.println(count);
                        }
                    }
                    """)
                .starterCodePython("""
                    s = input().lower()
                    print(sum(1 for c in s if c in 'aeiou'))
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        string s; getline(cin, s);
                        int count = 0;
                        for (char c : s) {
                            c = tolower(static_cast<unsigned char>(c));
                            if (string("aeiou").find(c) != string::npos)
                                count++;
                        }
                        cout << count;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const s = fs.readFileSync(0, 'utf8').trim().toLowerCase();
                    console.log([...s].filter(c => 'aeiou'.includes(c)).length);
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"Hello World", "3", "true"},
                new String[]{"AEIOU", "5", "true"},
                new String[]{"rhythm", "0", "false"},
                new String[]{"Programming", "3", "false"});
    }

    private void seedLargestArrayElement() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Find the Largest Element")
                .slug("largest-array-element")
                .description("""
                    Given an array of integers, print its largest element.

                    Input format:
                    Line 1: n, the number of elements.
                    Line 2: n space-separated integers.

                    Output format:
                    Print the largest integer.

                    Example:
                    Input:
                    5
                    3 8 2 10 4
                    Output: 10
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.HCL)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int n = sc.nextInt();
                            int max = Integer.MIN_VALUE;
                            for (int i = 0; i < n; i++)
                                max = Math.max(max, sc.nextInt());
                            System.out.println(max);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    nums = list(map(int, input().split()))
                    print(max(nums))
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        int n; cin >> n;
                        int x, largest = INT_MIN;
                        for (int i = 0; i < n; i++) {
                            cin >> x;
                            largest = max(largest, x);
                        }
                        cout << largest;
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const a = fs.readFileSync(0, 'utf8').trim().split(/\\s+/).map(Number);
                    const n = a[0];
                    console.log(Math.max(...a.slice(1, n + 1)));
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"5\n3 8 2 10 4", "10", "true"},
                new String[]{"1\n-7", "-7", "true"},
                new String[]{"4\n-5 -2 -9 -1", "-1", "false"},
                new String[]{"3\n100 100 99", "100", "false"});
    }

    private void seedCheckPrimeNumber() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Check Prime Number")
                .slug("check-prime-number")
                .description("""
                    Given an integer n, determine whether it is prime.
                    A prime number is greater than 1 and has exactly two
                    positive divisors.

                    Input format:
                    A single integer n.

                    Output format:
                    Print true if n is prime, otherwise print false.

                    Example:
                    Input: 7
                    Output: true
                    """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.RECURSION)
                .company(CodingCompany.ACCENTURE)
                .starterCodeJava("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            int n = new Scanner(System.in).nextInt();
                            boolean prime = n >= 2;
                            for (int i = 2; (long)i * i <= n; i++)
                                if (n % i == 0) { prime = false; break; }
                            System.out.println(prime);
                        }
                    }
                    """)
                .starterCodePython("""
                    n = int(input())
                    prime = n >= 2
                    i = 2
                    while i * i <= n:
                        if n % i == 0:
                            prime = False
                            break
                        i += 1
                    print('true' if prime else 'false')
                    """)
                .starterCodeCpp("""
                    #include <bits/stdc++.h>
                    using namespace std;
                    int main() {
                        long long n; cin >> n;
                        bool prime = n >= 2;
                        for (long long i = 2; i * i <= n; i++)
                            if (n % i == 0) { prime = false; break; }
                        cout << (prime ? "true" : "false");
                    }
                    """)
                .starterCodeJavascript("""
                    const fs = require('fs');
                    const n = Number(fs.readFileSync(0, 'utf8').trim());
                    let prime = n >= 2;
                    for (let i = 2; i * i <= n; i++) {
                        if (n % i === 0) { prime = false; break; }
                    }
                    console.log(prime ? 'true' : 'false');
                    """)
                .build());

        saveTestCases(problem,
                new String[]{"7", "true", "true"},
                new String[]{"10", "false", "true"},
                new String[]{"2", "true", "false"},
                new String[]{"1", "false", "false"},
                new String[]{"-5", "false", "false"});
    }



    private void seedTwoSum() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Two Sum")
                .slug("two-sum")
                .description("""
                        Given an array of integers and a target value, return the indices of the two \
                        numbers such that they add up to the target.

                        Input format:
                        Line 1: n (size of the array)
                        Line 2: n space-separated integers
                        Line 3: target integer

                        Output format:
                        Two space-separated 0-based indices, in ascending order, of the two numbers that \
                        add up to target. Assume exactly one valid answer exists.

                        Example:
                        Input:
                        4
                        2 7 11 15
                        9
                        Output:
                        0 1
                        """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.ARRAYS)
                .company(CodingCompany.AMAZON)
                .timeLimitMs(2000)
                .memoryLimitMb(256)
                .starterCodeJava("""
                        import java.util.*;

                        public class Main {
                            public static void main(String[] args) {
                                Scanner scanner = new Scanner(System.in);
                                int n = Integer.parseInt(scanner.nextLine().trim());
                                StringTokenizer tokenizer = new StringTokenizer(scanner.nextLine());
                                int[] nums = new int[n];
                                for (int i = 0; i < n; i++) nums[i] = Integer.parseInt(tokenizer.nextToken());
                                int target = Integer.parseInt(scanner.nextLine().trim());

                                // Write your solution below
                                Map<Integer, Integer> seen = new HashMap<>();
                                for (int i = 0; i < n; i++) {
                                    int complement = target - nums[i];
                                    if (seen.containsKey(complement)) {
                                        System.out.println(seen.get(complement) + " " + i);
                                        return;
                                    }
                                    seen.put(nums[i], i);
                                }
                            }
                        }
                        """)
                .starterCodePython("""
                        def solve():
                            n = int(input())
                            nums = list(map(int, input().split()))
                            target = int(input())

                            # Write your solution below
                            seen = {}
                            for i, value in enumerate(nums):
                                complement = target - value
                                if complement in seen:
                                    print(seen[complement], i)
                                    return
                                seen[value] = i

                        solve()
                        """)
                .starterCodeCpp("""
                        #include <bits/stdc++.h>
                        using namespace std;

                        int main() {
                            int n;
                            cin >> n;
                            vector<int> nums(n);
                            for (int i = 0; i < n; i++) cin >> nums[i];
                            int target;
                            cin >> target;

                            // Write your solution below
                            unordered_map<int, int> seen;
                            for (int i = 0; i < n; i++) {
                                int complement = target - nums[i];
                                if (seen.count(complement)) {
                                    cout << seen[complement] << " " << i << endl;
                                    return 0;
                                }
                                seen[nums[i]] = i;
                            }
                            return 0;
                        }
                        """)
                .starterCodeJavascript("""
                        const readline = require('readline');
                        const rl = readline.createInterface({ input: process.stdin });
                        let lines = [];
                        rl.on('line', (line) => lines.push(line));
                        rl.on('close', () => {
                            const n = parseInt(lines[0].trim());
                            const nums = lines[1].trim().split(' ').map(Number);
                            const target = parseInt(lines[2].trim());

                            // Write your solution below
                            const seen = new Map();
                            for (let i = 0; i < n; i++) {
                                const complement = target - nums[i];
                                if (seen.has(complement)) {
                                    console.log(seen.get(complement) + ' ' + i);
                                    return;
                                }
                                seen.set(nums[i], i);
                            }
                        });
                        """)
                .build());

        saveTestCases(problem,
                new String[]{"4\n2 7 11 15\n9", "0 1", "true"},
                new String[]{"3\n3 2 4\n6", "1 2", "true"},
                new String[]{"5\n1 5 3 8 2\n10", "2 3", "false"},
                new String[]{"2\n0 4\n4", "0 1", "false"}
        );
    }

    private void seedValidParentheses() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Valid Parentheses")
                .slug("valid-parentheses")
                .description("""
                        Given a string containing only the characters '(', ')', '{', '}', '[' and ']', \
                        determine if the input string is valid. An input string is valid if brackets are \
                        closed by the same type of bracket and in the correct order.

                        Input format:
                        A single line containing the string.

                        Output format:
                        Print "true" if the string is valid, otherwise print "false".
                        """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.STRINGS)
                .company(CodingCompany.GOOGLE)
                .timeLimitMs(2000)
                .memoryLimitMb(256)
                .starterCodeJava("""
                        import java.util.*;

                        public class Main {
                            public static void main(String[] args) {
                                Scanner scanner = new Scanner(System.in);
                                String input = scanner.nextLine();

                                // Write your solution below
                                Deque<Character> stack = new ArrayDeque<>();
                                Map<Character, Character> pairs = Map.of(')', '(', ']', '[', '}', '{');
                                boolean valid = true;
                                for (char c : input.toCharArray()) {
                                    if (c == '(' || c == '[' || c == '{') {
                                        stack.push(c);
                                    } else if (pairs.containsKey(c)) {
                                        if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                                            valid = false;
                                            break;
                                        }
                                    }
                                }
                                System.out.println(valid && stack.isEmpty());
                            }
                        }
                        """)
                .starterCodePython("""
                        def solve():
                            s = input()

                            # Write your solution below
                            stack = []
                            pairs = {')': '(', ']': '[', '}': '{'}
                            valid = True
                            for c in s:
                                if c in '([{':
                                    stack.append(c)
                                elif c in pairs:
                                    if not stack or stack.pop() != pairs[c]:
                                        valid = False
                                        break
                            print('true' if valid and not stack else 'false')

                        solve()
                        """)
                .starterCodeCpp("""
                        #include <bits/stdc++.h>
                        using namespace std;

                        int main() {
                            string s;
                            getline(cin, s);

                            // Write your solution below
                            stack<char> st;
                            map<char, char> pairs = {{')', '('}, {']', '['}, {'}', '{'}};
                            bool valid = true;
                            for (char c : s) {
                                if (c == '(' || c == '[' || c == '{') {
                                    st.push(c);
                                } else if (pairs.count(c)) {
                                    if (st.empty() || st.top() != pairs[c]) { valid = false; break; }
                                    st.pop();
                                }
                            }
                            cout << ((valid && st.empty()) ? "true" : "false") << endl;
                            return 0;
                        }
                        """)
                .starterCodeJavascript("""
                        const readline = require('readline');
                        const rl = readline.createInterface({ input: process.stdin });
                        let lines = [];
                        rl.on('line', (line) => lines.push(line));
                        rl.on('close', () => {
                            const s = lines[0];

                            // Write your solution below
                            const stack = [];
                            const pairs = { ')': '(', ']': '[', '}': '{' };
                            let valid = true;
                            for (const c of s) {
                                if ('([{'.includes(c)) {
                                    stack.push(c);
                                } else if (pairs[c]) {
                                    if (stack.pop() !== pairs[c]) { valid = false; break; }
                                }
                            }
                            console.log(valid && stack.length === 0 ? 'true' : 'false');
                        });
                        """)
                .build());

        saveTestCases(problem,
                new String[]{"()", "true", "true"},
                new String[]{"()[]{}", "true", "true"},
                new String[]{"(]", "false", "false"},
                new String[]{"([)]", "false", "false"},
                new String[]{"{[]}", "true", "false"}
        );
    }

    private void seedReverseArray() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Reverse a Linked List")
                .slug("reverse-linked-list")
                .description("""
                        A singly linked list is represented as a sequence of integers. Reverse the list \
                        and print the reversed sequence.

                        Input format:
                        Line 1: n (number of nodes)
                        Line 2: n space-separated integers representing the list from head to tail

                        Output format:
                        The reversed list as space-separated integers.
                        """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.LINKED_LIST)
                .company(CodingCompany.MICROSOFT)
                .timeLimitMs(2000)
                .memoryLimitMb(256)
                .starterCodeJava("""
                        import java.util.*;

                        public class Main {
                            public static void main(String[] args) {
                                Scanner scanner = new Scanner(System.in);
                                int n = Integer.parseInt(scanner.nextLine().trim());
                                StringTokenizer tokenizer = new StringTokenizer(scanner.nextLine());
                                int[] nodes = new int[n];
                                for (int i = 0; i < n; i++) nodes[i] = Integer.parseInt(tokenizer.nextToken());

                                // Write your solution below
                                StringBuilder sb = new StringBuilder();
                                for (int i = n - 1; i >= 0; i--) {
                                    sb.append(nodes[i]);
                                    if (i > 0) sb.append(" ");
                                }
                                System.out.println(sb);
                            }
                        }
                        """)
                .starterCodePython("""
                        def solve():
                            n = int(input())
                            nodes = list(map(int, input().split()))

                            # Write your solution below
                            print(' '.join(map(str, reversed(nodes))))

                        solve()
                        """)
                .starterCodeCpp("""
                        #include <bits/stdc++.h>
                        using namespace std;

                        int main() {
                            int n;
                            cin >> n;
                            vector<int> nodes(n);
                            for (int i = 0; i < n; i++) cin >> nodes[i];

                            // Write your solution below
                            reverse(nodes.begin(), nodes.end());
                            for (int i = 0; i < n; i++) cout << nodes[i] << (i + 1 < n ? " " : "\\n");
                            return 0;
                        }
                        """)
                .starterCodeJavascript("""
                        const readline = require('readline');
                        const rl = readline.createInterface({ input: process.stdin });
                        let lines = [];
                        rl.on('line', (line) => lines.push(line));
                        rl.on('close', () => {
                            const n = parseInt(lines[0].trim());
                            const nodes = lines[1].trim().split(' ').map(Number);

                            // Write your solution below
                            console.log(nodes.reverse().join(' '));
                        });
                        """)
                .build());

        saveTestCases(problem,
                new String[]{"5\n1 2 3 4 5", "5 4 3 2 1", "true"},
                new String[]{"1\n7", "7", "true"},
                new String[]{"3\n9 1 5", "5 1 9", "false"}
        );
    }

    private void seedMaximumSubarray() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Maximum Subarray")
                .slug("maximum-subarray")
                .description("""
                        Given an integer array, find the contiguous subarray (containing at least one \
                        number) which has the largest sum, and print that sum.

                        Input format:
                        Line 1: n (size of the array)
                        Line 2: n space-separated integers (may be negative)

                        Output format:
                        The maximum subarray sum.
                        """)
                .difficulty(CodingDifficulty.MEDIUM)
                .topic(CodingTopic.DP)
                .company(CodingCompany.TCS)
                .timeLimitMs(2000)
                .memoryLimitMb(256)
                .starterCodeJava("""
                        import java.util.*;

                        public class Main {
                            public static void main(String[] args) {
                                Scanner scanner = new Scanner(System.in);
                                int n = Integer.parseInt(scanner.nextLine().trim());
                                StringTokenizer tokenizer = new StringTokenizer(scanner.nextLine());
                                int[] nums = new int[n];
                                for (int i = 0; i < n; i++) nums[i] = Integer.parseInt(tokenizer.nextToken());

                                // Write your solution below
                                int maxSum = nums[0];
                                int current = nums[0];
                                for (int i = 1; i < n; i++) {
                                    current = Math.max(nums[i], current + nums[i]);
                                    maxSum = Math.max(maxSum, current);
                                }
                                System.out.println(maxSum);
                            }
                        }
                        """)
                .starterCodePython("""
                        def solve():
                            n = int(input())
                            nums = list(map(int, input().split()))

                            # Write your solution below
                            max_sum = current = nums[0]
                            for value in nums[1:]:
                                current = max(value, current + value)
                                max_sum = max(max_sum, current)
                            print(max_sum)

                        solve()
                        """)
                .starterCodeCpp("""
                        #include <bits/stdc++.h>
                        using namespace std;

                        int main() {
                            int n;
                            cin >> n;
                            vector<int> nums(n);
                            for (int i = 0; i < n; i++) cin >> nums[i];

                            // Write your solution below
                            int maxSum = nums[0];
                            int current = nums[0];
                            for (int i = 1; i < n; i++) {
                                current = max(nums[i], current + nums[i]);
                                maxSum = max(maxSum, current);
                            }
                            cout << maxSum << endl;
                            return 0;
                        }
                        """)
                .starterCodeJavascript("""
                        const readline = require('readline');
                        const rl = readline.createInterface({ input: process.stdin });
                        let lines = [];
                        rl.on('line', (line) => lines.push(line));
                        rl.on('close', () => {
                            const n = parseInt(lines[0].trim());
                            const nums = lines[1].trim().split(' ').map(Number);

                            // Write your solution below
                            let maxSum = nums[0];
                            let current = nums[0];
                            for (let i = 1; i < n; i++) {
                                current = Math.max(nums[i], current + nums[i]);
                                maxSum = Math.max(maxSum, current);
                            }
                            console.log(maxSum);
                        });
                        """)
                .build());

        saveTestCases(problem,
                new String[]{"9\n-2 1 -3 4 -1 2 1 -5 4", "6", "true"},
                new String[]{"1\n1", "1", "true"},
                new String[]{"5\n-1 -2 -3 -4 -5", "-1", "false"},
                new String[]{"5\n5 4 -1 7 8", "23", "false"}
        );
    }

    private void seedFibonacci() {
        CodingProblem problem = problemRepository.save(CodingProblem.builder()
                .title("Nth Fibonacci Number")
                .slug("nth-fibonacci-number")
                .description("""
                        Given an integer n, print the nth Fibonacci number, where F(0) = 0, F(1) = 1, and \
                        F(n) = F(n-1) + F(n-2) for n > 1.

                        Input format:
                        A single integer n (0 <= n <= 40).

                        Output format:
                        The nth Fibonacci number.
                        """)
                .difficulty(CodingDifficulty.EASY)
                .topic(CodingTopic.RECURSION)
                .company(CodingCompany.INFOSYS)
                .timeLimitMs(2000)
                .memoryLimitMb(256)
                .starterCodeJava("""
                        import java.util.*;

                        public class Main {
                            public static void main(String[] args) {
                                Scanner scanner = new Scanner(System.in);
                                int n = Integer.parseInt(scanner.nextLine().trim());

                                // Write your solution below
                                long a = 0, b = 1;
                                for (int i = 0; i < n; i++) {
                                    long next = a + b;
                                    a = b;
                                    b = next;
                                }
                                System.out.println(a);
                            }
                        }
                        """)
                .starterCodePython("""
                        def solve():
                            n = int(input())

                            # Write your solution below
                            a, b = 0, 1
                            for _ in range(n):
                                a, b = b, a + b
                            print(a)

                        solve()
                        """)
                .starterCodeCpp("""
                        #include <bits/stdc++.h>
                        using namespace std;

                        int main() {
                            int n;
                            cin >> n;

                            // Write your solution below
                            long long a = 0, b = 1;
                            for (int i = 0; i < n; i++) {
                                long long next = a + b;
                                a = b;
                                b = next;
                            }
                            cout << a << endl;
                            return 0;
                        }
                        """)
                .starterCodeJavascript("""
                        const readline = require('readline');
                        const rl = readline.createInterface({ input: process.stdin });
                        let lines = [];
                        rl.on('line', (line) => lines.push(line));
                        rl.on('close', () => {
                            const n = parseInt(lines[0].trim());

                            // Write your solution below
                            let a = 0n, b = 1n;
                            for (let i = 0; i < n; i++) {
                                const next = a + b;
                                a = b;
                                b = next;
                            }
                            console.log(a.toString());
                        });
                        """)
                .build());

        saveTestCases(problem,
                new String[]{"0", "0", "true"},
                new String[]{"1", "1", "true"},
                new String[]{"10", "55", "false"},
                new String[]{"20", "6765", "false"}
        );
    }

    private void saveTestCases(CodingProblem problem, String[]... testCaseData) {
        int orderIndex = 0;
        for (String[] data : testCaseData) {
            testCaseRepository.save(CodingTestCase.builder()
                    .problem(problem)
                    .input(data[0])
                    .expectedOutput(data[1])
                    .sample(Boolean.parseBoolean(data[2]))
                    .orderIndex(orderIndex++)
                    .build());
        }
    }
}
